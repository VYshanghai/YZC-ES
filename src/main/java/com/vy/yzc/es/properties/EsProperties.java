package com.vy.yzc.es.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Edward
 * @Date: 2021/3/26 00:23
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "es")
public class EsProperties {

	private String host;

	private String username;

	private String password;
}
