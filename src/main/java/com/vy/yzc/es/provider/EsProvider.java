package com.vy.yzc.es.provider;

import com.vy.yzc.es.dal.repo.model.EsOffersPO;
import com.vy.yzc.es.dto.EsOffersSaveReq;
import com.vy.yzc.es.dto.EsSearchVO;
import com.vy.yzc.es.dto.OffersFilterReq;
import com.vy.yzc.es.dto.OffersKeywordRecommendReq;
import com.vy.yzc.es.dto.OffersNearReq;
import com.vy.yzc.es.dto.OffersSearchReq;
import com.vy.yzc.es.service.OffersElasticsearchService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: vikko
 * @Date: 2021/3/26 01:59
 * @Description:
 */

@RestController
@RequestMapping("/provider/es/offers")
@AllArgsConstructor
public class EsProvider {

	private final OffersElasticsearchService offersElasticsearchService;

	/**
	* 关键词联想
	* @param req
	* @return
	*/
	@PostMapping("/keyword/recommend")
	public EsSearchVO<String> keywordRecommend(@RequestBody OffersKeywordRecommendReq req){
		return offersElasticsearchService.keywordRecommend(req);
	}

	/**
	 * 根据关键词分页搜索
	 */
	@PostMapping("/search/keyword/{userId}")
	public EsSearchVO<Long> searchKeyword(@RequestBody OffersSearchReq req, @PathVariable(required = false)Long userId){
		return offersElasticsearchService.searchKeyword(req,userId);
	}

	/**
	 * 根据条件过滤筛选
	 */
	@PostMapping("/search/filter/{userId}")
	public EsSearchVO<Long> searchFilter(@RequestBody OffersFilterReq req, @PathVariable(required = false) Long userId){
		return offersElasticsearchService.searchFilter(req,userId);
	}


	/**
	 * 批量新增
	 */
	@PostMapping("/batch/save")
	public Boolean batchSave(@RequestBody List<EsOffersPO> records){
		return offersElasticsearchService.batchSave(records);
	}

	/**
	 * 批量删除
	 */
	@PostMapping("/batch/delete")
	public Boolean batchDelete(@RequestBody List<Long> offersIds){
		return offersElasticsearchService.batchDelete(offersIds);
	}


	/**
	 * 附近的爆料
	 */
	@PostMapping("/near/{userId}")
	public EsSearchVO<Long> near(@RequestBody OffersNearReq req, @PathVariable(required = false) Long userId){
		return offersElasticsearchService.near(req, userId);
	}

	/**
	 * 更新deleted状态
	 */
	@PostMapping("/update/deleted/state")
	public Boolean updateDeletedState(@RequestBody List<Long> offersIds, @PathVariable(required = false) Integer deleted){
		return offersElasticsearchService.updateDeletedState(offersIds,deleted);
	}

	/**
	 * 批量新增req
	 */
	@PostMapping("/save/reqs")
	public Boolean saveReqs(@RequestBody List<EsOffersSaveReq> reqs){
		return offersElasticsearchService.saveReqs(reqs);
	}
}
