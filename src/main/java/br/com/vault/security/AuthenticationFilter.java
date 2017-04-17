package br.com.vault.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import br.com.vault.security.exception.AuthenticationException;

@Component
public class AuthenticationFilter implements Filter {

  private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);

  private AuthenticationService authenticationService;

  @Autowired
  public void setAuthenticationService(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
        filterConfig.getServletContext());
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    String apiKey = httpRequest.getHeader("api-key");
    String token = httpRequest.getHeader("api-token");

    logger.info("================ Filtering ================");

    try {
      authenticationService.authenticate(apiKey, token);
      chain.doFilter(request, response);
    } catch (AuthenticationException e) {
      ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  @Override
  public void destroy() {
  }
}
