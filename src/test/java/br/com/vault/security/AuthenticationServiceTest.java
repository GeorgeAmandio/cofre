package br.com.vault.security;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.vault.controller.AuthenticationController;
import br.com.vault.data.model.Application;
import br.com.vault.data.model.Authentication;
import br.com.vault.data.repository.ApplicationRepository;
import br.com.vault.data.repository.AuthenticationRepository;
import br.com.vault.security.AuthenticationService;
import br.com.vault.security.exception.AuthenticationException;
import br.com.vault.security.exception.InvalidApiKeyException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationServiceTest {
	
	@InjectMocks
	AuthenticationController authenticationControllerMock;
	@InjectMocks
	AuthenticationService authenticationServiceMock;
	
	@Autowired
	AuthenticationRepository authenticationRepository;
	@Autowired
	ApplicationRepository applicationRepository;

	@Mock
	ApplicationRepository applicationRepositoryMock;
	@Mock
	AuthenticationRepository authenticationRepositoryMock;

	@Value("${test.api.key}")
	String apiKey;
	@Value("${test.api.token}")
	String apiToken;
	
	Application application;
	Authentication authentication;
	
	@Before
	public void init(){
		application = new Application();
		application.setApiKey(apiKey);
		application.setName("Cliente JUnit");
		applicationRepository.save(application);
		authentication = new Authentication(apiKey, apiToken);
	    authenticationRepository.save(authentication);
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testa_o_metodo_que_deleta_uma_sessao_authetication_existente() throws InvalidApiKeyException{
		
		doNothing().when(authenticationRepositoryMock).delete(authentication.getId());
		when(applicationRepositoryMock.findByApiKey(apiKey)).thenReturn(application);
		when(authenticationRepositoryMock.findByApiKey(apiKey)).thenReturn(authentication);
		when(authenticationRepositoryMock.save(authentication)).thenReturn(authentication);
		
		authenticationServiceMock.createSession(apiKey);
		
		verify(authenticationRepositoryMock).delete(authentication.getId());
	}

	@Test(expected = InvalidApiKeyException.class)
	public void espera_o_erro_Invalid_Api_Key_Exception_do_metodo_create_session() throws InvalidApiKeyException{
		when(applicationRepositoryMock.findByApiKey(apiKey)).thenReturn(null);
		authenticationServiceMock.createSession(apiKey);
	}

	@Test(expected = AuthenticationException.class)
	public void espera_o_erro_Authentication_Exception_do_metodo_authenticate() throws InvalidApiKeyException, AuthenticationException{
		when(authenticationRepositoryMock.findByApiKeyAndToken(apiKey, apiToken)).thenReturn(null);
		authenticationServiceMock.authenticate(apiKey, apiToken);
	}
	
	@After
	public void finish(){
		authenticationRepository.delete(authentication);
		applicationRepository.delete(application);
	}
	
}
