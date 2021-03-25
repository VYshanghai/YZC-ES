package com.vy.yzc.es.dto;

import com.vy.yzc.es.service.base.BaseEsPageReq;
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
public class OffersKeywordRecommendReq extends BaseEsPageReq {

	@NotNull
	private String keyword;

}
