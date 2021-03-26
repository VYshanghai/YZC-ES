package com.vy.yzc.es.toolkit;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author: vikko
 * @Date: 2021/3/1 17:53
 * @Description: 可以序列化的function
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
