package com.vy.yzc.es;

import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import com.vy.yzc.es.dto.EsSearchVO;
import com.vy.yzc.es.dto.OffersNearReq;
import com.vy.yzc.es.service.OffersElasticsearchService;
import java.math.BigDecimal;
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
//
//	@Test
//	public void delete(){
//		offersRepository.deleteById(1234L);
//	}
//
//	@Test
//	public  void init(){
//		EsOffersPO build = EsOffersPO.builder()
//				.offersId(1234L)
//				.title("title")
//				.content("content")
//				.infoSource(1)
//				.postType(1)
//				.categoryType(1L)
//				.categoryName("name")
//				.location("31.231706,121.472644")
//				.deleted(1)
//				.build();
//		offersRepository.save(build);
//	}

	@Test
	public void test(){
		Iterable<EsOffersPO> all = offersRepository.findAll();
		all.forEach(System.out::println);
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

}
