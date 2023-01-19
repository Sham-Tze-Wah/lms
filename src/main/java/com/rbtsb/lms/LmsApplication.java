package com.rbtsb.lms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "LMS API", version = "2.0", description = "LMS info"))
public class LmsApplication {

	//this is the application
	//Token : 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
	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

//	@Configuration
//	@EnableWebMvc
//	public class WebConfig extends WebMvcConfigurerAdapter {
//
//		@Override
//		public void addCorsMappings(CorsRegistry registry) {
//			registry.addMapping("/**");
//		}
//	}

}
