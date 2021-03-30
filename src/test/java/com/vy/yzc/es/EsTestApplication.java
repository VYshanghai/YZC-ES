package com.vy.yzc.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.util.Lists;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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

	/*@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;*/

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Test
	public void test(){

		BoolQueryBuilder result = new BoolQueryBuilder();
		result.must(QueryBuilders.matchQuery("title", "运动"));
		Iterable<EsOffersPO> search = offersRepository.search(result, PageRequest.of(0,10));

		Lists.newArrayList(search).forEach(System.out::println);
//		System.out.println(offersRepository.findById(1375070232629309470L));
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

}
