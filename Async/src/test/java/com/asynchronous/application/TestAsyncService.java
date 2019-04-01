package com.asynchronous.application;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.ConnectException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

/*
 * To run testGetDomainViaStubbing successfully, comment out resttemplate and testGetDomainServiceViaMock method
 * testGetDomainServiceViaMock method will run on it's own
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class TestAsyncService {

	@Autowired
	private AsyncService asyncService;
	
	@Autowired
	private CallerService callerService;
	
	@MockBean
	private RestTemplate restTemplate;
	
	//@Rule
	//public WireMockRule wireMockRule = new WireMockRule(8080);
	
	/**
	 * Using wiremock stubbing of external rest call
	 * This cannot be successfully run without commenting out the restTemplate mockbean (error in port used)
	 * @throws Exception 
	 */
	/*@Test
	public void testGetDomainViaStubbing() throws Exception {
		callerService.setServerUrl("http://localhost:8080");
		stubFor(get(urlEqualTo("/api/sync/domains/"+1))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("" +
								"{\r\n" + 
								"\"id\": 1,\r\n" + 
								"\"data\": \"stubData\",\r\n" + 
								"\"desc\": \"stubDescription\"\r\n" + 
								"}")));
		Domain domain = callerService.getDomain(1);
		assertEquals(1, domain.getId());
		assertEquals("stubData", domain.getData());
		assertEquals("stubDescription", domain.getDesc());
		
		CompletableFuture<ExternalResponse<Domain>> comDomain = asyncService.getDomain(1);
		CompletableFuture.allOf(comDomain).join();
		Domain domainAsync = comDomain.get().getData();
		assertEquals(1, domainAsync.getId());
		assertEquals("stubData", domainAsync.getData());
		assertEquals("stubDescription", domainAsync.getDesc());
	}*/
	
	@Before
	public void setUp() {
		System.out.println("TEST: "+ callerService.getServerUrl());
		callerService.setServerUrl("http://localhost:8080");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "AUTHORIZATION-TOKEN-FROM-CLIENT");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		when(restTemplate.exchange(URI.create(callerService.getServerUrl()+"/api/sync/domains/"+ 1), HttpMethod.GET, entity, Domain.class))
		.thenReturn(ResponseEntity.ok(new Domain(1, "dataMocked", "descriptionMocked")));
	}
	
	/**
	 * Using mockito for mocking restTemplate
	 * @throws Exception 
	 */
	@Test
	public void testGetDomainServiceViaMock() throws Exception {
		assertEquals("http://localhost:8080", callerService.getServerUrl());
		Domain domainResponse = callerService.getDomain(1);
		assertEquals(1, domainResponse.getId());
		assertEquals("dataMocked", domainResponse.getData());
		assertEquals("descriptionMocked", domainResponse.getDesc());
		
		CompletableFuture<ExternalResponse<Domain>> comDomain = asyncService.getDomain(1);
		CompletableFuture.allOf(comDomain).join();
		Domain domainAsync = comDomain.get().getData();
		assertEquals(1, domainAsync.getId());
		assertEquals("dataMocked", domainAsync.getData());
		assertEquals("descriptionMocked", domainAsync.getDesc());
	}
	
	@Test
	public void testGetDomainServiceViaMock2() throws Exception {
		assertEquals("http://localhost:8080", callerService.getServerUrl());
		Domain domainResponse = callerService.getDomain(1);
		assertEquals(1, domainResponse.getId());
		assertEquals("dataMocked", domainResponse.getData());
		assertEquals("descriptionMocked", domainResponse.getDesc());
		
		CompletableFuture<ExternalResponse<Domain>> comDomain = asyncService.getDomain(1);
		CompletableFuture.allOf(comDomain).join();
		Domain domainAsync = comDomain.get().getData();
		assertEquals(1, domainAsync.getId());
		assertEquals("dataMocked", domainAsync.getData());
		assertEquals("descriptionMocked", domainAsync.getDesc());
	}
	
}
