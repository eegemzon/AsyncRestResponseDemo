package com.asynchronous.application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.timeout.TimeoutException;

@RequestMapping("/api/async/")
@RestController
public class AsyncCallerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCallerController.class);
	private AsyncService asyncService;
	
	public AsyncCallerController(AsyncService asyncService) {
		this.asyncService = asyncService;
	}
	
	@GetMapping("/data/{id}")
	public ResponseEntity<Response> getData(@RequestHeader("Authorization") String authorization, @PathVariable int id) {
		System.out.println("Authorization: " + authorization);
		Response response = null;
		try {
			LOGGER.info("Get Data {}", id);
			CompletableFuture<ExternalResponse<Domain>> futureDomain = asyncService.getDomain(id);
			CompletableFuture<ExternalResponse<Entity>> futureEntity = asyncService.getEntity(id);
			LOGGER.info("DOING SOME OTHER PROCESSING HERE...");
			CompletableFuture.allOf(futureDomain, futureEntity).join();
			LOGGER.info("Completing execution...");
			if(futureDomain.get().isDefaultValue() && futureEntity.get().isDefaultValue()) {	
				throw new Exception("External calls failed");
			}
			response = new Response(futureDomain.get().getData(), futureEntity.get().getData());
		} catch (InterruptedException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (ExecutionException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (CompletionException e) {
			if(e.getCause() instanceof TimeoutException) {
				LOGGER.info("TIMEOUT CALLING THE 3rd party");
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
			} else if(e.getCause() instanceof HttpStatusCodeException) {
				HttpStatusCodeException exc = (HttpStatusCodeException) e.getCause();
				if (exc.getStatusCode() == HttpStatus.BAD_REQUEST) {
					String responseString = exc.getResponseBodyAsString();
					ObjectMapper mapper = new ObjectMapper();
					try {
						Fault result = mapper.readValue(responseString, Fault.class);
						System.out.println(result.toString());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
					} catch(Exception ex) {
						System.out.println(ex.getMessage());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
					}
				}
			}
			LOGGER.info("Exception on controller", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/mono/data/{id}")
	public ResponseEntity<Response> getMonoData(@PathVariable int id) throws InterruptedException, ExecutionException {
		LOGGER.info("Get Data {}", id);
		Response response = null;
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
	@Override
	public String toString() {
		return "Response [domain=" + domain + ", entity=" + entity + "]";
	}
	
}



