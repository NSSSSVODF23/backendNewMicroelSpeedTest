package com.microel.speedtest.services.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public JwtSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable().cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//FIXME Включить проверку токена
                .and().authorizeRequests().antMatchers("/upload/**", "/measure/**", "/public/**", "/ping/**", "/subscriptions").permitAll()
                .antMatchers("/graphql").authenticated()
//                .and().authorizeRequests().antMatchers("/upload/**", "/measure/**", "/public/**", "/ping/**", "/subscriptions", "/graphql").permitAll()
                .and().addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
