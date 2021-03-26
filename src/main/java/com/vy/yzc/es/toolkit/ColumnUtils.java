package com.vy.yzc.es.toolkit;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: vikko
 * @Date: 2021/3/1 17:44
 * @Description: 通过可序列化的function，拿到对应的字段名
 * （主要是为了防止写错）
 */
public class ColumnUtils {

	public static <T> String getName(SFunction<T, ?> fn) {
		// 从function取出序列化方法
		Method writeReplaceMethod;
		try {
			writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

		// 从序列化方法取出序列化的lambda信息
		boolean isAccessible = writeReplaceMethod.isAccessible();
		writeReplaceMethod.setAccessible(true);
		SerializedLambda serializedLambda;
		try {
			serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		writeReplaceMethod.setAccessible(isAccessible);

		// 从lambda信息取出method、field、class等
		String fieldName = serializedLambda.getImplMethodName().substring("get".length());
		fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());

		return fieldName;
	}
}
