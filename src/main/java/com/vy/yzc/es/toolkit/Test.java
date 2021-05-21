package com.vy.yzc.es.toolkit;

import java.util.Map;

/**
 * @author: vikko
 * @Date: 2021/5/21 11:59
 * @Description:
 */
public class Test {

	public static void main(String[] args) throws Exception {
		User user = new User(2L, "vikko", 23, "giegie", null);
		Map<String, Object> map = BeanUtils.objectToMap(user,false);

		System.out.println(map.get("name").toString());

	}

}


 class User{
	private Long id;

	private String name;

	private Integer age;

	private String title;

	private Integer price;

	public User(Long id, String name, Integer age, String title, Integer price) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.title = title;
		this.price = price;
	}
}
