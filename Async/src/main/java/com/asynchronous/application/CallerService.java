package com.asynchronous.application;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * Synchronous REST call
 * @author eineil.eric.c.gemzon
 *
 */

@Service
public class CallerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CallerService.class);
	private static final String DOMAINS_ENDPOINT = "/api/sync/domains/";
	private static final String ENTITIES_ENDPOINT = "/api/sync/entities/";

	@Value("${serverUrl}")
	private String serverUrl;
	
	private RestTemplate restTemplate;
	private WebClient webClient;
	
	public CallerService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.webClient = WebClient.create("http://localhost:8081");
	}
	
	public Domain getDomain(int id) {
		LOGGER.info("Calling domains URL");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "AUTHORIZATION-TOKEN-FROM-CLIENT");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		System.out.println("ServerURL:" + serverUrl);
		ResponseEntity<Domain> domain = restTemplate.exchange(URI.create(serverUrl + DOMAINS_ENDPOINT + id), 
				HttpMethod.GET, entity, Domain.class);
		return domain.getBody();
	}
	
	public Entity getEntity(int id) {
		LOGGER.info("Calling entities URL");
		ResponseEntity<Entity> entity = restTemplate.getForEntity(URI.create(serverUrl + ENTITIES_ENDPOINT + id), Entity.class);	
		return entity.getBody();
	}
	
	public Mono<Domain> getDomainFlux(int id) {
		LOGGER.info("WebClient - Calling domains URL");
		Mono<Domain> domainMono = webClient.get()
				.uri("/api/sync/domains/")
				.retrieve()
				.bodyToMono(Domain.class);
		return domainMono;
	}
	
	public Mono<Entity> getEntityFlux(int id) {
		LOGGER.info("WebClient - Calling entities URL");
		Mono<Entity> entityMono = webClient.get()
				.uri("/api/sync/entities/")
				.retrieve()
				.bodyToMono(Entity.class);
		return entityMono;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
}
