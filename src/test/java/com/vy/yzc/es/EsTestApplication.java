package com.vy.yzc.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import com.vy.yzc.es.dto.EsSearchVO;
import com.vy.yzc.es.dto.OffersNearReq;
import com.vy.yzc.es.service.OffersElasticsearchService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Edward
 * @Date: 2021/3/26 00:34
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTestApplication {

	@Autowired
	private OffersRepository offersRepository;

//	@Test
//	public void test(){
//
//		BoolQueryBuilder result = new BoolQueryBuilder();
//		result.must(QueryBuilders.matchQuery("title", "运动"));
//		Iterable<EsOffersPO> search = offersRepository.search(result, PageRequest.of(0,10));
//
//		Lists.newArrayList(search).forEach(System.out::println);
////		System.out.println(offersRepository.findById(1375070232629309470L));
//	}
//

	@Test
	public void delete(){
		offersRepository.deleteById(1234L);
	}

	@Test
	public  void init(){
		EsOffersPO build = EsOffersPO.builder()
				.offersId(1234L)
				.title("title")
				.content("content")
				.infoSource(1)
				.postType(1)
				.categoryType(1L)
				.categoryName("name")
				.location("31.231706,121.472644")
				.deleted(1)
				.build();
		offersRepository.save(build);
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
		result.must(QueryBuilders.wildcardQuery("title", "*250g*"));
		Iterable<EsOffersPO> search = offersRepository.search(result);
		search.forEach(System.out::println);

	}

	@Autowired
	OffersElasticsearchService offersElasticsearchService;

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
}
