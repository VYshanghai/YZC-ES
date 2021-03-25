package com.vy.yzc.es.config;

import com.vy.yzc.es.properties.EsProperties;
import java.util.Objects;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.TerminalClientConfigurationBuilder;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @Author: Edward
 * @Date: 2021/3/26 00:22
 * @Description:
 */
@Configuration
public class EsRestConfiguration extends AbstractElasticsearchConfiguration {

	@Autowired
	private EsProperties esProperties;

	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		TerminalClientConfigurationBuilder terminalClientConfigurationBuilder = ClientConfiguration
				.builder()
				.connectedTo(esProperties.getHost());
		if (Objects.nonNull(esProperties.getPassword()) && Objects
				.nonNull(esProperties.getUsername())) {
			terminalClientConfigurationBuilder
					.withBasicAuth(esProperties.getUsername(), esProperties.getPassword());
		}
		final ClientConfiguration clientConfiguration = terminalClientConfigurationBuilder
				.build();
		return RestClients.create(clientConfiguration).rest();
	}
}
