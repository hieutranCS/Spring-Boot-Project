package com.spring.security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.spring.security.demo.security.AdminLoginFailureHandler;
import com.spring.security.demo.security.AdminLoginSuccessHandler;
import com.spring.security.demo.security.UserLoginFailureHandler;
import com.spring.security.demo.security.UserLoginSuccessHandler;

@Configuration
public class SecurityConfig {

	@Bean
	public static AuthenticationSuccessHandler adminHandler() {
		return new AdminLoginSuccessHandler();
	}

	@Bean
	public static AuthenticationSuccessHandler userHandler() {
		return new UserLoginSuccessHandler();
	}

	@Bean
	public static AuthenticationFailureHandler adminFailureHandler() {
		return new AdminLoginFailureHandler();
	}

	@Bean
	public static AuthenticationFailureHandler userFailureHandler() {
		return new UserLoginFailureHandler();
	}

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Configuration
//	public static class RegularSecurityConfig extends WebSecurityConfigurerAdapter {
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//
//			http.authorizeRequests().antMatchers("/css/**", "/admin/login").permitAll().antMatchers("/").permitAll()
//					.antMatchers("/admin").hasAnyAuthority("EMPLOYEE", "ADMIN").anyRequest().authenticated().and()
//					.formLogin().loginPage("/admin/login").successHandler(adminHandler())
//					.failureHandler(adminFailureHandler()).permitAll().and().logout().logoutUrl("/logout").permitAll();
//
//		}
//	}

	@Configuration
	@Order(1)
	public static class RegularSecurityConfig {

		@Bean
		protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

			http.antMatcher("/admin/**").authorizeRequests()
					.antMatchers("/css/**", "/admin/login", "/admin/register", "/admin/register/save",
							"/admin/edit/save", "/admin/edit**")
					.permitAll().antMatchers("/").permitAll().antMatchers("/admin", "/employee")
					.hasAnyAuthority("EMPLOYEE", "ADMIN").anyRequest().authenticated().and().formLogin()
					.loginPage("/admin/login").successHandler(adminHandler()).failureHandler(adminFailureHandler())
					.permitAll().and().logout().logoutUrl("/logout").invalidateHttpSession(true)
					.deleteCookies("JSESSIONID").permitAll().and().csrf().disable();
			return http.build();

		}
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

//	@Configuration
//	@Order(1)
//	public static class SpecialSecurityConfig extends WebSecurityConfigurerAdapter {
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.antMatcher("/user/**").authorizeRequests().antMatchers("/css/**",  "/user/register", "/user/login").permitAll()
//			.antMatchers("/user").hasAnyAuthority("USER").anyRequest()
//					.authenticated().and().formLogin().loginPage("/user/login").successHandler(userHandler())
//					.failureHandler(userFailureHandler()).permitAll().and().logout().logoutUrl("/logout").permitAll();
//
//		}
//	}

	@Configuration
	@Order(2)
	public static class SpecialSecurityConfig {

		@Bean
		protected SecurityFilterChain configure2(HttpSecurity http) throws Exception {
			http.antMatcher("/user/**").authorizeRequests()
					.antMatchers("/user/login", "/user/register", "/account/register").permitAll().and().formLogin()
					.loginPage("/user/login").successHandler(userHandler()).failureHandler(userFailureHandler())
					.permitAll().and().logout().logoutUrl("/logout").invalidateHttpSession(true)
					.deleteCookies("JSESSIONID").permitAll().and().csrf().disable();

			return http.build();

		}
	}

}
