package com.asynchronous.application;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Wraps the Synchronous CallerService to make it asynchronous with CompletableFuture
 * @author eineil.eric.c.gemzon
 *
 */

@Service
public class AsyncService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);
	private CallerService callerService;

	public AsyncService(CallerService callerService) {
		this.callerService = callerService;
	}
	
	@Async
	public CompletableFuture<Domain> getDomain(int id) {
		LOGGER.info("calling callerService.getDomain");
		Domain domain = callerService.getDomain(id);
		return CompletableFuture.completedFuture(domain);
	}
	
	@Async
	public CompletableFuture<Entity> getEntity(int id) {
		LOGGER.info("Calling callerService.getEntity");
		Entity entity = callerService.getEntity(id);
		return CompletableFuture.completedFuture(entity);
	}
	
}
