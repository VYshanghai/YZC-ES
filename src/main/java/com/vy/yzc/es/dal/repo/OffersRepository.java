package com.vy.yzc.es.dal.repo;

import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: Edward
 * @Date: 2021/3/26 00:31
 * @Description:
 */
public interface OffersRepository extends ElasticsearchRepository<EsOffersPO, Long> {

}
