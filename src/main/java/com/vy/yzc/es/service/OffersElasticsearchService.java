package com.vy.yzc.es.service;

import com.vy.yzc.es.dal.repo.OffersRepository;
import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import com.vy.yzc.es.dto.EsOffersSaveReq;
import com.vy.yzc.es.dto.EsSearchVO;
import com.vy.yzc.es.dto.OffersFilterReq;
import com.vy.yzc.es.dto.OffersKeywordRecommendReq;
import com.vy.yzc.es.dto.OffersNearReq;
import com.vy.yzc.es.dto.OffersSearchReq;
import com.vy.yzc.es.service.base.BaseEsService;
import java.util.List;

/**
 * @author: vikko
 * @Date: 2021/3/4 16:52
 * @Description:
 */
public interface OffersElasticsearchService extends BaseEsService<EsOffersPO, OffersRepository> {

	/**
	 * 关键词联想
	 * @param req
	 * @return
	 */
	EsSearchVO<String> keywordRecommend(OffersKeywordRecommendReq req);

	/**
	 * 根据关键词分页搜索
	 * @param req
	 * @param userId
	 * @return
	 */
	EsSearchVO<Long> searchKeyword(OffersSearchReq req, Long userId);

	/**
	 * 根据条件过滤筛选
	 * @param req
	 * @param userId
	 * @return
	 */
	EsSearchVO<Long> searchFilter(OffersFilterReq req, Long userId);


	/**
	 * 批量新增
	 * @param records
	 * @return
	 */
	Boolean batchSave(List<EsOffersPO> records);

	/**
	 * 批量删除
	 * @param offersIds
	 * @return
	 */
	Boolean batchDelete(List<Long> offersIds);


	/**
	 * 附近的爆料
	 * @param req
	 * @param userId
	 * @return
	 */
	EsSearchVO<Long> near(OffersNearReq req, Long userId);

	/**
	 * 更新deleted状态
	 * @param offersIds
	 * @param deleted
	 * @return
	 */
	Boolean updateDeletedState(List<Long> offersIds, Integer deleted);

	/**
	 * 批量新增req
	 * @param reqs
	 * @return
	 */
	Boolean saveReqs(List<EsOffersSaveReq> reqs);
}
