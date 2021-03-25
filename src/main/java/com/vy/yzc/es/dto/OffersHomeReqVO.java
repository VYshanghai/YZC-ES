package com.vy.yzc.es.dto;

import com.vy.yzc.es.service.base.BaseEsPageReq;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: Edward
 * @Date: 2021/3/16 13:34
 * @Description:
 */
@Data
@Validated
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OffersHomeReqVO extends BaseEsPageReq {


	@NotNull
//	@ApiModelProperty(value = "类型 1: 用户爆料线上 2: 用户爆料线下 3: 优惠雷达线上 4: 优惠雷达线下")
	private Integer type;

//	@ApiModelProperty("类别Id")
	private Long categoryId;

	@DecimalMin("0")
	@DecimalMax("90.0")
//	@ApiModelProperty(value = "纬度", example = "33.22")
	private BigDecimal lat;

	@DecimalMin("0")
	@DecimalMax("180.0")
//	@ApiModelProperty(value = "经度", example = "22.33")
	private BigDecimal lng;

	@Min(1)
	@Max(5)
//	@ApiModelProperty(value = "优惠标签：0 未知 1 满减 2 折扣 3 满赠 4 低价 5 优惠套餐。", example = "1")
	private Integer offersTag;

//	@ApiModelProperty(value = "附近的距离", example = "500")
	private Integer distance;


}
