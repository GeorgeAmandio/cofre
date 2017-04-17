package br.com.vault.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import br.com.vault.security.AuthenticationFilter;

@Configuration
public class WebApplicationConfig extends WebMvcConfigurerAdapter {

  @Bean
  public FilterRegistrationBean authenticationFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new AuthenticationFilter());
    registration.addUrlPatterns("/vault/*");
    return registration;
  }
}