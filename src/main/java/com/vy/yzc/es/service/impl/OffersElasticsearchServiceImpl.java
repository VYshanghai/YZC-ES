package com.vy.yzc.es.service.impl;

import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import com.vy.yzc.es.dto.EsOffersSaveReq;
import com.vy.yzc.es.dto.EsSearchVO;
import com.vy.yzc.es.dto.OffersFilterReq;
import com.vy.yzc.es.dto.OffersKeywordRecommendReq;
import com.vy.yzc.es.dto.OffersNearReq;
import com.vy.yzc.es.dto.OffersSearchReq;
import com.vy.yzc.es.dto.OffersSearchType;
import com.vy.yzc.es.service.OffersElasticsearchService;
import com.vy.yzc.es.service.base.BaseEsServiceImpl;
import com.vy.yzc.es.toolkit.ColumnUtils;
import com.vy.yzc.es.toolkit.SFunction;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author: vikko
 * @Date: 2021/3/4 16:53
 * @Description: todo 代码非常丑陋，待优化： 1.构建builder优化，通过function的方式传入字段 2.baseEsService封装基础代码：对字段搜索封装queryWrapper
 * 3.构建query时尝试用策略模式 4.indices和type用注解引入
 */
@Service
@Slf4j
public class OffersElasticsearchServiceImpl extends
		BaseEsServiceImpl<EsOffersPO, OffersRepository> implements OffersElasticsearchService {

	@Autowired
	OffersRepository esOffersRepository;

	@Resource(name = "esOffersSaveExecutor")
	private TaskExecutor esOffersSaveExecutor;

	public static final String categoryIdListFormat = "*%s*";

	@Override
	public EsSearchVO<String> keywordRecommend(OffersKeywordRecommendReq req) {
		Page<String> page = page(req,
				q -> getRecommendBuilder(q.getKeyword()),
				getScoreSortBuilder(),
				pos -> pos.stream().map(EsOffersPO::getTitle).collect(Collectors.toList()));
		return page2VO(page);
	}

	private <T> EsSearchVO<T> page2VO(Page<T> source) {
		return EsSearchVO.<T>builder()
				.currentPage(source.getNumber() + 1)
				.pageSize(source.getSize())
				.results(source.getContent())
				.total((int) source.getTotalElements())
				.build();
	}

	@Override
	public EsSearchVO<Long> searchFilter(OffersFilterReq req) {
		Page<Long> page = page(req,
				q -> getFilterQuery(req),
				getFilterSortBuilder(req.getLat(), req.getLng(), req.getPostType()),
				pos -> pos.stream()
						.sorted(Comparator.comparing(EsOffersPO::getCreatedTime).reversed())
						.map(EsOffersPO::getOffersId)
						.collect(Collectors.toList()));
		return page2VO(page);
	}

	@Override
	public EsSearchVO<Long> searchKeyword(OffersSearchReq req) {
		OffersSearchType offersSearchType = Optional
				.ofNullable(OffersSearchType.getByCode(req.getType()))
				.orElseThrow(() -> new RuntimeException("未知的枚举"));

		Page<Long> page = page(req,
				q -> getKeywordBuilder(offersSearchType, q.getKeyword()),
				getScoreSortBuilder(),
				pos -> pos.stream().map(EsOffersPO::getOffersId).collect(Collectors.toList()));
		return page2VO(page);
	}

	@Override
	public EsSearchVO<Long> near(OffersNearReq req) {

		if (Objects.isNull(req.getLat()) || Objects.isNull(req.getLng())) {
			//todo throw new
			throw new RuntimeException("location info");
		}
		Page<Long> page = page(req, q -> getNearQuery(),
				getFilterSortBuilder(req.getLat(), req.getLng(), 2),
				pos -> pos.stream().map(EsOffersPO::getOffersId).collect(Collectors.toList()));
		log.info("附近的爆料id ： 【{}】", page.getContent());
		return page2VO(page);
	}

	@Override
	public Boolean batchSave(List<EsOffersPO> records) {
		log.info("------------开始写入爆料数据到es，offerIds：[{}]------------",
				records.stream().map(EsOffersPO::getOffersId).collect(Collectors.toList()));
		if (CollectionUtils.isEmpty(records)) {
			return false;
		}
		CompletableFuture.runAsync(() -> esOffersRepository.saveAll(records), esOffersSaveExecutor);
//		esOffersRepository.saveAll(records);
		log.info("------------写入爆料数据到es:结束------------");
		return true;
	}

	@Override
	public Boolean batchDelete(List<Long> offersIds) {
		if (CollectionUtils.isEmpty(offersIds)) {
			return false;
		}
		esOffersRepository
				.deleteAll(offersIds.stream().map(id -> EsOffersPO.builder().offersId(id).build()).collect(
						Collectors.toList()));
		return true;
	}

	public QueryBuilder getKeywordBuilder(OffersSearchType type, String keyword) {
		BoolQueryBuilder result = getCommonBuilder();
		result.must(QueryBuilders
				.multiMatchQuery(keyword, columnOf(EsOffersPO::getTitle), columnOf(EsOffersPO::getContent),
						columnOf(EsOffersPO::getCategoryName), columnOf(EsOffersPO::getShopName))
				.analyzer("ik_smart"));
//		result.must(QueryBuilders.matchPhraseQuery(columnOf(EsOffersPO::getTitle), keyword));
		if (!Objects.equals(type, OffersSearchType.ALL)) {
			result.must(QueryBuilders
					.termQuery(columnOf(EsOffersPO::getPostType), type.getCode()));
		}
		return result;
//		return QueryBuilders.functionScoreQuery(result, getWeightQuery(keyword));
	}

	public QueryBuilder getRecommendBuilder(String keyword) {
		BoolQueryBuilder result = getCommonBuilder();
		result.must(QueryBuilders.matchQuery(columnOf(EsOffersPO::getTitle), keyword));
		return result;
	}

	public FilterFunctionBuilder[] getWeightQuery(String searchContent) {
		return new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
				new FunctionScoreQueryBuilder.FilterFunctionBuilder(
						QueryBuilders.matchQuery(columnOf(EsOffersPO::getCategoryName), searchContent),
						ScoreFunctionBuilders.weightFactorFunction(1)),
				new FunctionScoreQueryBuilder.FilterFunctionBuilder(
						QueryBuilders.termQuery(columnOf(EsOffersPO::getTitle), searchContent),
						ScoreFunctionBuilders.weightFactorFunction(1)),
				new FunctionScoreQueryBuilder.FilterFunctionBuilder(
						QueryBuilders.termQuery(columnOf(EsOffersPO::getContent), searchContent),
						ScoreFunctionBuilders.weightFactorFunction(1)),
				new FunctionScoreQueryBuilder.FilterFunctionBuilder(
						QueryBuilders.matchQuery(columnOf(EsOffersPO::getShopName), searchContent),
						ScoreFunctionBuilders.weightFactorFunction(1)),
		};
	}

	public BoolQueryBuilder getCommonBuilder() {
		BoolQueryBuilder result = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery(columnOf(EsOffersPO::getDeleted), 0));
//		result.must(getTimeValidStartQuery());
		result.must(getTimeValidEndQuery());
		return result;
	}

	public QueryBuilder getFilterQuery(OffersFilterReq req) {
		BoolQueryBuilder result = getCommonBuilder();
		if (Objects.nonNull(req.getDistance())) {
			if (Objects.isNull(req.getLat()) || Objects.isNull(req.getLng())) {
				//todo
				throw new RuntimeException("location info");
			}
			result.filter(getGeoBuilder(req.getLat(), req.getLng(), req.getDistance()));
		}
		if (Objects.nonNull(req.getCategoryType())) {
			result.must(
					QueryBuilders.wildcardQuery(columnOf(EsOffersPO::getCategoryIdList),
							String.format(categoryIdListFormat, req.getCategoryType())));
		}
		if (Objects.nonNull(req.getCouponType())) {
			result
					.must(QueryBuilders.termQuery(columnOf(EsOffersPO::getCouponType), req.getCouponType()));
		}
		if (Objects.nonNull(req.getPlatform())) {
			result
					.must(QueryBuilders.termQuery(columnOf(EsOffersPO::getPlatform), req.getPlatform()));
		}
		if (Objects.nonNull(req.getInfoSource())) {
			result
					.must(QueryBuilders.termQuery(columnOf(EsOffersPO::getInfoSource), req.getInfoSource()));
		}
		if (Objects.nonNull(req.getPostType())) {
			result.must(QueryBuilders.termQuery(columnOf(EsOffersPO::getPostType), req.getPostType()));
		}
		return result;
	}

	public QueryBuilder getNearQuery() {
		BoolQueryBuilder result = getCommonBuilder();
		result.must(QueryBuilders.termQuery(columnOf(EsOffersPO::getPostType), 2));
		return result;
	}

	private QueryBuilder getTimeValidStartQuery() {
		return QueryBuilders.rangeQuery(columnOf(EsOffersPO::getValidStartTime))
				.lt(System.currentTimeMillis());
	}

	private QueryBuilder getTimeValidEndQuery() {
		return QueryBuilders.rangeQuery(columnOf(EsOffersPO::getValidEndTime))
				.gt(System.currentTimeMillis());
	}

	private QueryBuilder getGeoBuilder(BigDecimal lat, BigDecimal lng, Integer distance) {
		return new GeoDistanceQueryBuilder(columnOf(EsOffersPO::getLocation))
				.distance(String.valueOf(distance), DistanceUnit.METERS)
				.point(new GeoPoint(lat.doubleValue(), lng.doubleValue()));
	}

	private SortBuilder getFilterSortBuilder(BigDecimal lat, BigDecimal lng, Integer postType) {
		boolean isLocate = Objects.nonNull(lat) && Objects.nonNull(lng);
		if (postType == 2 && isLocate) {
			//线下 todo enum
			GeoDistanceSortBuilder disSortBuilder = new GeoDistanceSortBuilder(
					columnOf(EsOffersPO::getLocation),
					lat.doubleValue(), lng.doubleValue());
			disSortBuilder.order(SortOrder.ASC);
			disSortBuilder.unit(DistanceUnit.METERS);
			return disSortBuilder;
		}
		return new FieldSortBuilder(columnOf(EsOffersPO::getValidEndTime)).order(SortOrder.ASC);
	}

	private SortBuilder getScoreSortBuilder() {
		return SortBuilders.scoreSort();
	}

	private <T> SortBuilder getFieldSortBuilder(SFunction<T, ?> columnFunc, SortOrder sortOrder) {
		return SortBuilders.fieldSort(columnOf(columnFunc)).order(sortOrder);
	}

	public static <T> String columnOf(SFunction<T, ?> fn) {
		return ColumnUtils.getName(fn);
	}

	@Override
	public Boolean updateDeletedState(List<Long> offersIds, Integer deleted) {
		//todo 缺失记录log
		CompletableFuture.runAsync(() -> {
			Iterable<EsOffersPO> pos = esOffersRepository.findAllById(offersIds);
			pos.forEach(po -> po.setDeleted(deleted));
			esOffersRepository.saveAll(pos);
		}, esOffersSaveExecutor);
		return true;
	}

	@Override
	public Boolean saveReqs(List<EsOffersSaveReq> reqs) {
		if(CollectionUtils.isEmpty(reqs)){
			return false;
		}
		CompletableFuture.runAsync(() -> {
			List<EsOffersPO> list = reqs.stream().map(source -> {
				EsOffersPO saveEntity = new EsOffersPO();
				defaultCopy(source, saveEntity);
				return saveEntity;
			}).collect(Collectors.toList());
			esOffersRepository.saveAll(list);
		}, esOffersSaveExecutor);
		return true;
	}

	/**
	 * todo
	 */
	public static void defaultCopy(Object source, Object target) {
		BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
		beanCopier.copy(source, target, null);
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String s = columnOf(EsOffersPO::getTitle);
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
}
