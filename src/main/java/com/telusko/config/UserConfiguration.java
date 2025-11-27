package com.telusko.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class UserConfiguration {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//Disable CSRF Token Generation
		http.csrf(customizer->customizer.disable());
		//Don't include add-player end point
		http.authorizeHttpRequests(authorizeHttp->authorizeHttp.requestMatchers("register","login")
				.permitAll().anyRequest().authenticated());
		//user can make request from anywhere like postman, browser.
		http.httpBasic(Customizer.withDefaults());
		//now request is stateless
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
	
	@Bean
	public AuthenticationProvider authProvider()
	{
	
		
		DaoAuthenticationProvider daoProvider=new DaoAuthenticationProvider();
		//describe encription method
		daoProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		//providing userDetailsService Object.
		daoProvider.setUserDetailsService(userDetailsService);
		//daoProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		return daoProvider;
		
		
	}
	
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	
	
	
	
	
	
	
//	@Bean
//	public UserDetailsService userDetails()
//	{
//		
//		UserDetails user = User.withDefaultPasswordEncoder()
//		.username("ravi123@gmail.com")
//		.password("telusko123")
//		.roles("user")
//		.build();
//		
//		UserDetails user2 = User.withDefaultPasswordEncoder()
//				.username("shivam123@gmail.com")
//				.password("telusko222")
//				.roles("user")
//				.build();
//		
//		return new InMemoryUserDetailsManager(user,user2);
//		
//		
//	}
}
	
	
	
	
