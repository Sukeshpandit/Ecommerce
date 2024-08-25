package com.app.dev.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.app.dev.JsonWebTokenServices.JwtAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class AppConfig {
	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	JwtAuthenticationFilter jwtFilter;
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
				http
				.csrf(csrf->csrf.disable())
				.authorizeHttpRequests(authorize ->{
					authorize.requestMatchers("/users/**" ,"/users/resend/**").permitAll();
					authorize.requestMatchers("/products").authenticated();
					authorize.anyRequest().authenticated();
					
				})
				.headers(headers->headers.frameOptions(frameops->frameops.sameOrigin()))
				.sessionManagement(session->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
				
				return http.build();
	}
	
	@Bean
	public UrlBasedCorsConfigurationSource corsConfiguration() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedMethods(List.of("GET" ,"POST" , "PUT" , "DELETE"));
		config.setAllowedOriginPatterns(List.of("/**"));
		config.setAllowedHeaders(List.of("Authorization" , "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return  source;
	}
}
