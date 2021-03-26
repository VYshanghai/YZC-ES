package com.vy.yzc.es.config;

import com.vy.yzc.es.properties.EsProperties;
import java.time.Duration;
import java.util.Objects;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.TerminalClientConfigurationBuilder;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.http.HttpHeaders;

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
				.connectedTo(esProperties.getHost())
				.withConnectTimeout(Duration.ofSeconds(esProperties.getConnectTimeout()))
				.withSocketTimeout(Duration.ofSeconds(esProperties.getSocketTimeout()));
		if (Objects.nonNull(esProperties.getPassword()) && Objects
				.nonNull(esProperties.getUsername())) {
			HttpHeaders defaultHeaders = new HttpHeaders();
			defaultHeaders.setBasicAuth(esProperties.getUsername(), esProperties.getPassword());
			terminalClientConfigurationBuilder
					.withDefaultHeaders(defaultHeaders)
					.withBasicAuth(esProperties.getUsername(), esProperties.getPassword());
		}
		final ClientConfiguration clientConfiguration = terminalClientConfigurationBuilder
				.build();
		return RestClients.create(clientConfiguration).rest();
	}

}
