package com.microel.speedtest.common.configurations;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://192.168.0.103:4200");
		config.addAllowedOrigin("http://192.168.0.115:4200");
		config.addAllowedOrigin("http://192.168.0.101:4200");
		config.addAllowedOrigin("http://192.168.0.102:4200");
		config.addAllowedOriginPattern("http://192.168.0.*:*");
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedOrigin("http://10.1.3.150:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		source.registerCorsConfiguration("/**", config);
		// source.registerCorsConfiguration("/graphql", config);

		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
}
