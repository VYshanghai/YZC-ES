package com.vy.yzc.es.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: vikko
 * @Date: 2021/3/26 01:27
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsSearchVO<T> {

	private Integer total;

	private Integer currentPage;

	private Integer pageSize;

	private List<T> results;
}
