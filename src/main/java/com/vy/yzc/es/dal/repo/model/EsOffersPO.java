package com.vy.yzc.es.dal.repo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * @author: vikko
 * @Date: 2021/3/2 14:09
 * @Description:
 */
@Data
@Document(indexName = "visva-yzc-beta", type = "offers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsOffersPO {

//	private String score;

	@Id
	private Long offersId;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String title;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
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
	 * 当前商品属性的所有id的string，用英文逗号隔开
	 */
	private String categoryIdList;

	/**
	 * 分类名称
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String categoryName;

	/**
	 * 店名
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String shopName;

	/**
	 *格式：纬度,经度
	 * 例如：31.231706,121.472644
	 * 英文逗号
	 */
	@GeoPointField
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

	/**
	 * cityName
	 */
	private String cityName;

	/**
	 * 商圈id，用英文逗号隔开
	 */
	private String regionIdList;

	/**
	 * 价格
	 */
	private Integer price;

	/**
	 * 折扣减去的价格 = 原价 - 现价
	 */
	private Integer discountPrice;

}
