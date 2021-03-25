package com.vy.yzc.es.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: vikko
 * @Date: 2021/3/2 14:09
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsOffersSaveReq {

	private Long offersId;

	private String title;

	private String content;

	/**
	 * 发布人类型：1用户和商家 2系统抓取
	 */
	private Integer infoSource;

	/**
	 * 1online 2offline
	 */
	private Integer postType;

	/**
	 * 爆料的商品类别分类（只存二级分类）
	 */
	private Long categoryType;

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 店名
	 */
	private String shopName;

	/**
	 *格式：纬度,经度
	 * 例如：31.231706,121.472644
	 * 英文逗号
	 */
	private String location;

	/**
	 * 优惠标签：0 未知 1 满减 2 折扣 3 满赠 4 低价 5 优惠套餐。默认 0
	 */
	private Integer couponType;

	/**
	 * 点击次数
	 */
	private Integer clickCount;

	/**
	 * 创建时间戳
	 */
	private Long createdTime;

	/**
	 * 开始时间戳
	 */
	private Long validStartTime;

	/**
	 * 过期时间戳
	 */
	private Long validEndTime;

	/**
	 * 0 正常 1被删除
	 */
	private Integer deleted;

	/**
	 * 信息来源平台：0 未知 1 羊值厂 2 淘宝 3 京东 4 拼多多 5 唯品会 6 美团 7 其它。默认 0
	 */
	private Integer platform;

}
