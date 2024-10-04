package com.iti.thesis.helicopter.thesis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iti.thesis.helicopter.thesis.security.jwt.JwtRequestFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

@Configuration
public class SecurityConfiguration {
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@SuppressWarnings("unused")
	private CustomerUserDetail customerUserDetail;

	public SecurityConfiguration(CustomerUserDetail custom) {
		this.customerUserDetail = custom;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable()).cors(withDefaults())
				.authorizeHttpRequests(requests -> requests
						.antMatchers("/api/user/login", "/api/user/reset-password", "/api/user/logout").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT filter
		return httpSecurity.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder(MStringUtil.EMPTY, 255); // Custom encoder
	}
}
