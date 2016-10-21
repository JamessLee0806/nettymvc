package com.baocloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(scanBasePackages={"com.baocloud"})
public class BootStrap extends WebMvcConfigurerAdapter{
	public static void main(String[] args) {
		SpringApplication app=new SpringApplication(BootStrap.class);
		app.run(args);
		
	}
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addRedirectViewController("/", "/readingList");
    }
}
