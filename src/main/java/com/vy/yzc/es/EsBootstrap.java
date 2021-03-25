package com.vy.yzc.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @Author: Edward
 * @Date: 2021/3/26 00:18
 * @Description:
 */
@SpringBootApplication
@EnableElasticsearchRepositories
public class EsBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(EsBootstrap.class, args);
	}

}
