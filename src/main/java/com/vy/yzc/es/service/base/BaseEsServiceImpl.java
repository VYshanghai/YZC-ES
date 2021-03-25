package com.vy.yzc.es.service.base;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: Edward
 * @Date: 2021/3/16 14:15
 * @Description:
 */
public abstract class BaseEsServiceImpl<T, Repo extends ElasticsearchRepository<T, Long>> implements
		BaseEsService<T, Repo> {

	@Autowired
	protected Repo repo;

	protected Class<T> poClazz;

	@SuppressWarnings("unchecked")
	public BaseEsServiceImpl() {
		poClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@Override
	public <Q extends BaseEsPageReq, R> Page<R> page(Q q, Function<Q, QueryBuilder> qb, SortBuilder sb,
			Function<List<T>, List<R>> converter) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
				.withQuery(qb.apply(q))
				.withSort(sb)
				.withTypes(getTypes())
				.withIndices(getIndices());
		if (Objects.isNull(q.getPageSize()) || Objects.isNull(q.getStartPage())) {
			builder.withPageable(NativeSearchQuery.DEFAULT_PAGE);
		} else {
			builder.withPageable(PageRequest.of(q.getStartPage() - 1, q.getPageSize()));
		}
		Page<T> pageResult = repo.search(builder.build());
		List<R> rs = Optional.ofNullable(pageResult).map(Slice::getContent).map(converter)
				.orElse(new ArrayList<>());
		AggregatedPageImpl ts = new AggregatedPageImpl(rs, pageResult.getPageable(),
				pageResult.getTotalElements());
		return ts;
	}

	protected String getIndices() {
		Document document = poClazz.getAnnotation(Document.class);
		if (Objects.nonNull(document)) {
			return document.indexName();
		}
		return null;
	}

	protected String getTypes() {
		Document document = poClazz.getAnnotation(Document.class);
		if (Objects.nonNull(document)) {
			return document.type();
		}
		return null;
	}
}
