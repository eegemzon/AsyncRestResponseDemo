package com.asynchronous.application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Wraps the Synchronous CallerService to make it asynchronous with
 * CompletableFuture
 * 
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
	public CompletableFuture<ExternalResponse<Domain>> getDomain(int id) {
		LOGGER.info("calling callerService.getDomain");
		return CompletableFuture.supplyAsync(() -> callerService.getDomain(id))
				.orTimeout(25, TimeUnit.SECONDS)
				.handle((value, ex) -> {
					if (ex == null) {
						System.out.println("no exception occurred");
						return new ExternalResponse<Domain>(false, value);
					}
					return new ExternalResponse<Domain>(true, new Domain(1, "defaultData", "defaultDesc"));
				});
	}

	@Async
	public CompletableFuture<ExternalResponse<Entity>> getEntity(int id) {
		LOGGER.info("Calling callerService.getEntity");
		return CompletableFuture.supplyAsync(() -> callerService.getEntity(id))
				.orTimeout(25, TimeUnit.SECONDS)
				.handle((value, ex) -> {
					if (ex == null) {
						System.out.println("no exception occurred");
						return new ExternalResponse<Entity>(false, value);
					}
					return new ExternalResponse<Entity>(true, new Entity(2, "Default Descriptionentity"));
				});
	}

	public void something() {

	}

	// TODO: call the two mono service from caller service
}
