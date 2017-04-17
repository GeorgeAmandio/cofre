package br.com.vault.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.vault.controller.VaultController;
import br.com.vault.data.model.Application;
import br.com.vault.data.model.Authentication;
import br.com.vault.data.model.Card;
import br.com.vault.data.repository.ApplicationRepository;
import br.com.vault.data.repository.AuthenticationRepository;
import br.com.vault.data.repository.exception.CardNotFoundException;
import br.com.vault.data.repository.impl.CardRepositoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VaultControllerTest {

	@Autowired
	AuthenticationRepository authenticationRepository;
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Value("${test.api.key}")
	String apiKey;
	@Value("${test.api.token}")
	String apiToken;

	@Mock
	Application applicationMock;
	@Mock
	Card cardMock;
	@Mock
	CardRepositoryImpl cardRepositoryImplMock;
	@Mock
	ApplicationRepository applicationRepositoryMock;

	@InjectMocks
	VaultController vaultControllerMock;
	
	Authentication authentication;
	Application application;
	
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
	public void espera_o_retorno_OK_da_consulta_de_todos_os_cartoes() throws IOException{
		
		String url = "http://localhost:8080/v1/vault/cards?customerId=user23";
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("api-key", apiKey);
		headers.add("api-token", apiToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

	@Test
	public void espera_o_retorno_OK_da_consulta_de_apenas_um_cartao() throws IOException{
		
		String url = "http://localhost:8080/v1/vault/cards/cardId-52daa034-4780-4248-b3f7-1053e16df454";
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("api-key", apiKey);
		headers.add("api-token", apiToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

	@Test(expected = HttpClientErrorException.class)
	public void espera_o_erro_http_client_error_exception() throws IOException{
		
		String url = "http://localhost:8080/v1/vault/cards/cardId-52daa034-0000-0000-AAAA-1053e16df454";
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("api-key", apiKey);
		headers.add("api-token", apiToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
	}
	
	@Test
	public void espera_a_chamada_do_metodo_que_deleta_cartao() throws IOException, CardNotFoundException{
		
		when(cardRepositoryImplMock.findByCardId("cardId-52daa034-4780-4248-b3f7-1053e16df454")).thenReturn(cardMock);
		doNothing().when(cardMock).delete();
		doNothing().when(cardRepositoryImplMock).update(cardMock);
		
		vaultControllerMock.deleteByCardId("cardId-52daa034-4780-4248-b3f7-1053e16df454");

		verify(cardRepositoryImplMock).findByCardId("cardId-52daa034-4780-4248-b3f7-1053e16df454");
		verify(cardMock).delete();
		verify(cardRepositoryImplMock).update(cardMock);
		
	}

	@Test
	public void espera_a_chamada_do_metodo_que_adiciona_cartao() throws IOException{		
		doNothing().when(cardRepositoryImplMock).create(cardMock);
	
		vaultControllerMock.cardCreate(cardMock, "user23");
		
		verify(cardRepositoryImplMock).create(cardMock);
	}
	
	@Test
	public void espera_a_chamada_do_metodo_que_cadastra_cliente() throws IOException{		
		when(applicationRepositoryMock.save(applicationMock)).thenReturn(applicationMock);
		
		vaultControllerMock.createClient(applicationMock);
		
		verify(applicationRepositoryMock).save(applicationMock);
	}
	
	@After
	public void finish(){
		authenticationRepository.delete(authentication);
		applicationRepository.delete(application);
	}

}
