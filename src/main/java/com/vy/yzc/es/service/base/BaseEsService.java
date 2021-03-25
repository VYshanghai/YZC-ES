package com.vy.yzc.es.service.base;

import java.util.List;
import java.util.function.Function;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: Edward
 * @Date: 2021/3/16 14:13
 * @Description:
 */
public interface BaseEsService <T, Repo extends ElasticsearchRepository<T, Long>>{


	<Q extends BaseEsPageReq, R> Page<R> page(Q q, Function<Q, QueryBuilder> qb, SortBuilder sb,
			Function<List<T>, List<R>> converter);

}
