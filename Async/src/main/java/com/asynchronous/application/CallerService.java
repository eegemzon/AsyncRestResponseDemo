package com.asynchronous.application;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Synchronous REST call
 * @author eineil.eric.c.gemzon
 *
 */

@Service
public class CallerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CallerService.class);
	private RestTemplate restTemplate;
	
	public CallerService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public Domain getDomain(int id) {
		LOGGER.info("Calling domains URL");
		ResponseEntity<Domain> domain = restTemplate.getForEntity(URI.create("http://localhost:8081/api/sync/domains/" + id), Domain.class);
		return domain.getBody();
	}
	
	public Entity getEntity(int id) {
		LOGGER.info("Calling entities URL");
		ResponseEntity<Entity> entity = restTemplate.getForEntity(URI.create("http://localhost:8081/api/sync/entities/" + id), Entity.class);	
		return entity.getBody();
	}
}
