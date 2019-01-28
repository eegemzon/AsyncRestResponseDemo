package com.asynchronous.application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/async/")
@RestController
public class AsyncCallerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCallerController.class);
	private AsyncService asyncService;
	
	public AsyncCallerController(AsyncService asyncService) {
		this.asyncService = asyncService;
	}
	
	@GetMapping("/data/{id}")
	public ResponseEntity<Response> getData(@PathVariable int id) throws InterruptedException, ExecutionException {
		LOGGER.info("Get Data {}", id);
		Response response = null;
		
		CompletableFuture<Domain> futureDomain = asyncService.getDomain(id);
		CompletableFuture<Entity> futureEntity = asyncService.getEntity(id);
		
		LOGGER.info("DOING SOME OTHER PROCESSING HERE...");
		//....some other executions
		
		//Wait for all to finish execution
		CompletableFuture.allOf(futureDomain, futureEntity).join();
		LOGGER.info("Completing execution...");
		response = new Response(futureDomain.get(), futureEntity.get());
		return ResponseEntity.ok().body(response);
	}
	

}

class Response {
	private Domain domain;
	private Entity entity;
	public Response() {}
	public Response(Domain domain, Entity entity) {
		super();
		this.domain = domain;
		this.entity = entity;
	}
	public Domain getDomain() {
		return domain;
	}
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}



