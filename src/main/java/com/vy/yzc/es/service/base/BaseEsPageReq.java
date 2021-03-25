package com.vy.yzc.es.service.base;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: Edward
 * @Date: 2021/3/16 14:29
 * @Description:
 */
@Data
@Validated
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEsPageReq {
	@Min(1)
	@Max(Integer.MAX_VALUE)
	@NotNull
	protected Integer startPage = 1;

	@Min(10)
	@Max(500)
	@NotNull
	protected Integer pageSize = 20;
}
