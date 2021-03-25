package com.vy.yzc.es;

import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import org.assertj.core.util.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

	@Test
	public void test(){

		BoolQueryBuilder result = new BoolQueryBuilder();
		result.must(QueryBuilders.matchQuery("title", "运动"));
		Iterable<EsOffersPO> search = offersRepository.search(result, PageRequest.of(0,10));

		Lists.newArrayList(search).forEach(System.out::println);
//		System.out.println(offersRepository.findById(1375070232629309470L));
	}

}
