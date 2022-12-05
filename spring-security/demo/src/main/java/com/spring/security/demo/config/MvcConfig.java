package com.spring.security.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/user/login").setViewName("userlogin");
        registry.addViewController("/user/register").setViewName("userregister");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/admin/login").setViewName("adminlogin");
        registry.addViewController("/admin/register").setViewName("adminregister");
        registry.addViewController("/employee").setViewName("employeepage");

    }
    
    
}
