package com.vy.yzc.es.dto;

import com.vy.yzc.es.service.base.BaseEsPageReq;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

/**
 * @author: vikko
 * @Date: 2021/2/22 16:42
 * @Description:
 */
@Data
@Validated
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OffersSearchReq extends BaseEsPageReq {

	@NotNull
	private String keyword;

//	@ApiModelProperty(value = "搜索类型：0所有，1线上，2线下")
	@NotNull
	private Integer type;

//	@ApiModelProperty(value = "纬度")
	private BigDecimal lat;

//	@ApiModelProperty(value = "经度")
	private BigDecimal lng;



}
