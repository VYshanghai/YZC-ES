package com.vy.yzc.es.toolkit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: vikko
 * @Date: 2021/5/21 11:52
 * @Description:
 */
public class BeanUtils {

	public static Map<String, Object> objectToMap(Object obj, Boolean needNullValue) {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap(16);
		try {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				Object o = field.get(obj);
				if(needNullValue || Objects.nonNull(o)){
					map.put(field.getName(), o);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return map;
	}

}
