package com.vy.yzc.es.dto;

import java.util.Objects;

/**
 * @author: vikko
 * @Date: 2021/2/22 16:32
 * @Description:
 */
public enum OffersSearchType {
	ALL(0,"所有"),
	OFFLINE(1,"线下"),
	ONLINE(2,"线上"),
	;

	int code;

	String message;

	OffersSearchType(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public static OffersSearchType getByCode(int code){
		for (OffersSearchType item : OffersSearchType.values()) {
			if(Objects.equals(code, item.getCode())){
				return item;
			}
		}
		return null;
	}
}
