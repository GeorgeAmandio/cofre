package br.com.vault.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.vault.controller.AuthenticationController;
import br.com.vault.data.repository.AuthenticationRepository;
import br.com.vault.security.AuthenticationService;
import br.com.vault.security.exception.InvalidApiKeyException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationControllerTest {
	
	@InjectMocks
	AuthenticationController authenticationControllerMock;
	@Autowired
	AuthenticationRepository authenticationRepository;
	
	@Mock
	AuthenticationService authenticationServiceMock;

	
	@Before
	public void init(){
	    MockitoAnnotations.initMocks(this);
	}

	@Test(expected = HttpClientErrorException.class)
	public void espera_exception_por_falta_de_parametro_para_o_endpoint(){
		String url = "http://localhost:8080/v1/auth/token";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_ACCEPTABLE));
	}

	@Test
	public void testa_a_chamada_para_o_service() throws InvalidApiKeyException{
		
		when(authenticationServiceMock.createSession("")).thenReturn("");
		String response = authenticationControllerMock.generateToken("");
		assertThat(response, equalTo(""));
		verify(authenticationServiceMock).createSession("");
	}
	
	
	

}
