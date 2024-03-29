package com.vy.yzc.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import com.vy.yzc.es.dto.EsOffersSaveReq;
import com.vy.yzc.es.dto.EsSearchVO;
import com.vy.yzc.es.dto.OffersNearReq;
import com.vy.yzc.es.dto.OffersOfflineSearchReq;
import com.vy.yzc.es.service.OffersElasticsearchService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Edward
 * @Date: 2021/3/26 00:34
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("SIT")
public class EsTestApplication {

	@Autowired
	private OffersRepository offersRepository;


	@Test
	public  void init(){
		EsOffersPO build = EsOffersPO.builder()
				.offersId(1234L)
				.title("title")
				.content("content")
				.infoSource(1)
				.postType(2)
				.categoryType(1L)
				.categoryName("name")
				.location("31.231706,121.472644")
				.shopName("shopName")
				.discountPrice(0)
				.price(0)
				.validEndTime(System.currentTimeMillis())
				.validStartTime(System.currentTimeMillis())
				.deleted(0)
				.build();
		offersRepository.save(build);
	}

	@Test
	public void testSearch(){

		OffersOfflineSearchReq req = OffersOfflineSearchReq.builder()
				.platform(1)
				.lng(BigDecimal.valueOf(121.472644))
				.lat(BigDecimal.valueOf(36.123))
				.build();
		EsSearchVO<Long> longEsSearchVO = offersElasticsearchService.searchOffline(req);
		System.out.println(longEsSearchVO);
	}



	@Test
	public void batchDelete(){

		List<Long> ids = Lists.newArrayList(1380404321803317250L,
				1380472788019589121L,
				1380376717155119106L,
				1380402195949694977L,
				1379359778881679361L,
				1379360278993711106L,
				1380092460855734273L);
		ids.forEach(id->offersRepository.deleteById(id));
	}


	@Autowired
	OffersElasticsearchService offersElasticsearchService;
	@Test
	public void batchLogicDelete(){
		List<Long> offersIds = Lists.newArrayList(
				1382176193361227778L,1382177589183655937L);
		offersElasticsearchService.updateDeletedState(offersIds,1);
	}



	@Test
	public void delete(){
		offersRepository.deleteById(1234L);
	}

	/*@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;*/

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Test
	public void test(){
//		Iterable<EsOffersPO> all = offersRepository.findAll();
//		all.forEach(System.out::println);
		BoolQueryBuilder result = new BoolQueryBuilder();
		//模糊查询
//		result.must(QueryBuilders.wildcardQuery("title", "88元代100元代金券"));
		result.must(QueryBuilders.termsQuery("platform", "6"));
//		result.must(QueryBuilders.termsQuery("deleted", "0"));
		Iterable<EsOffersPO> search = offersRepository.search(result);
		List<Long> xkcIds = Lists.newArrayList(search).stream().map(EsOffersPO::getOffersId)
				.collect(Collectors.toList());
		xkcIds.parallelStream().forEach(id->offersRepository.deleteById(id));
//		Boolean aBoolean = offersElasticsearchService.batchDelete(xkcIds);
//		System.out.println(aBoolean);
		System.out.println("----");

	}
	@Test
	public void testOffers(){
		BoolQueryBuilder result = new BoolQueryBuilder();
		//模糊查询
//		result.must(QueryBuilders.wildcardQuery("title", "88元代100元代金券"));
		result.must(QueryBuilders.termsQuery("offersId", "1382577208063389699"));
//		Iterable<EsOffersPO> search = offersRepository.search(result);
		Optional<EsOffersPO> byId = offersRepository.findById(1392373010284011522L);
		System.out.println("----");

	}


	@Test
	public void  testNear(){
		//31.279878,121.429972
		OffersNearReq req = OffersNearReq.builder()
				.lat(BigDecimal.valueOf(31.279878))
				.lng(BigDecimal.valueOf(121.429972))
				.startPage(1)
				.pageSize(10)
				.build();
		EsSearchVO<Long> near = offersElasticsearchService.near(req);
		System.out.println(near);
	}

	@Test
	public void test2() throws IOException {

//		Map<String, Object> mapping = elasticsearchTemplate.getMapping("visva-yzc-beta", "offers");

		GetMappingsRequest getMappings = new GetMappingsRequest().indices("visva-yzc-beta");
		//调用获取
		GetMappingsResponse getMappingResponse = restHighLevelClient.indices()
				.getMapping(getMappings, RequestOptions.DEFAULT);
		//处理数据
		Map<String, MappingMetaData> allMappings = getMappingResponse.mappings();
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Map.Entry<String, MappingMetaData> indexValue : allMappings.entrySet()) {
			Map<String, Object> mapping = indexValue.getValue().sourceAsMap();
			Iterator<Entry<String, Object>> entries = mapping.entrySet().iterator();
			entries.forEachRemaining(stringObjectEntry -> {
				if (stringObjectEntry.getKey().equals("properties")) {
					Map<String, Object> value = (Map<String, Object>) stringObjectEntry.getValue();
					for (Map.Entry<String, Object> ObjectEntry : value.entrySet()) {
						Map<String, Object> map = new HashMap<>();
						String key = ObjectEntry.getKey();
						Map<String, Object> value1 = (Map<String, Object>) ObjectEntry.getValue();
						map.put(key, value1.get("type"));
						mapList.add(map);
					}
				}
			});
		}
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(mapList));
	}


	@Test
	public void test3(){
		Iterable<EsOffersPO> all = offersRepository.findAll();
		all.forEach(po -> {
			po.setCityName("上海");
		});
		offersRepository.saveAll(all);

		Iterable<EsOffersPO> all1 = offersRepository.findAll();
		all1.forEach(System.out::println);
	}


	@Test
	public void refreshCategoryData(){
		BoolQueryBuilder result = new BoolQueryBuilder();
		//模糊查询
		result.must(QueryBuilders.termsQuery("postType", "2"));
		Iterable<EsOffersPO> all = offersRepository.search(result);
		all.forEach(data->data.setCategoryIdList(String.valueOf(data.getCategoryType())));
		offersRepository.saveAll(all);
		all.forEach(System.out::println);
	}

	@Test
	public void testMatchPhaseQuery(){
		BoolQueryBuilder result = new BoolQueryBuilder();
		//模糊查询
		result.must(QueryBuilders.fuzzyQuery("title", "员单"));
		Iterable<EsOffersPO> all = offersRepository.search(result);
		all.forEach(System.out::println);
	}
	@Test
	public void testPrice(){
		BoolQueryBuilder result = new BoolQueryBuilder();
		//模糊查询
		result.must(QueryBuilders.rangeQuery("price").gt(0).lt(10000000));
		result.must(QueryBuilders.termsQuery("deleted", "1"));
		Iterable<EsOffersPO> all = offersRepository.search(result);
		ArrayList<EsOffersPO> esOffersPOS = Lists.newArrayList(all);
		esOffersPOS.forEach(data->{data.setDeleted(0);});
		offersRepository.saveAll(esOffersPOS);
		System.out.println(1);
	}

	@Test
	public void testClient() throws Exception{
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("title", "vikko 测试用restHighLevelClient更新字段");
		jsonMap.put("platform", -1);
		UpdateRequest request = new UpdateRequest("visva-yzc-beta", "offers", "1392361395788402733")
				.doc(jsonMap);
		UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
		GetResult getResult = response.getGetResult();

	}

	@Test
	public void testUpdateEs(){
		List<EsOffersSaveReq> reqs = Lists
				.newArrayList(1392328171557167159L, 1392328198438461626L, 1392328205396811847L).stream()
				.map(id -> {
					return EsOffersSaveReq.builder()
							.offersId(id)
							.deleted(1)
							.build();
				}).collect(Collectors.toList());
		offersElasticsearchService.updateNonNullValue(reqs);
	}

}
