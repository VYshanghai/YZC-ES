package com.vy.yzc.es.dto;

import com.vy.yzc.es.service.base.BaseEsPageReq;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author: vikko
 * @Date: 2021/3/24 19:47
 * @Description:
 */
@Data
public class OffersNearReq extends BaseEsPageReq {

	private BigDecimal lat;

	private BigDecimal lng;
}
