package com.asynchronous.application;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AsyncService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);
	private RestTemplate restTemplate;

	public AsyncService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Async
	public CompletableFuture<Domain> getDomain(int id) {
		LOGGER.info("Calling domains URL");
		ResponseEntity<Domain> domain = restTemplate.getForEntity(URI.create("http://localhost:8081/api/sync/domains/" + id), Domain.class);
		return CompletableFuture.completedFuture(domain.getBody());
	}
	
	@Async
	public CompletableFuture<Entity> getEntity(int id) {
		LOGGER.info("Calling entities URL");
		ResponseEntity<Entity> entity = restTemplate.getForEntity(URI.create("http://localhost:8081/api/sync/entities/" + id), Entity.class);	
		return CompletableFuture.completedFuture(entity.getBody());
	}
	
}
