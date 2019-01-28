package com.synchronous.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping("/api/sync")
@RestController
public class SynchronousController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronousController.class);
	
	@GetMapping("/domains/{id}")
	public ResponseEntity<Domain> getDomain(@PathVariable String id) throws InterruptedException {
		LOGGER.info("getting domain with ID {}", id);
		Domain domain = new Domain(Integer.parseInt(id), "sampleData" + id, "Description for data " + id);
		Thread.sleep(15000);
		LOGGER.info("Returning domain: {}", domain.toString());
		return ResponseEntity.ok(domain);
	}
	
	@GetMapping("/entities/{id}")
	public ResponseEntity<Entity> getEntity(@PathVariable String id) throws InterruptedException {
		LOGGER.info("getting domain with ID {}", id);
		Entity entity = new Entity(Integer.parseInt(id), "Description for data " + id);
		Thread.sleep(10000);
		LOGGER.info("Returning entity: {}", entity.toString());
		return ResponseEntity.ok(entity);
	}
	
}

class Domain {
	private int id;
	private String data;
	private String desc;
	public Domain() {}
	public Domain(int id, String data, String desc) {
		this.id   = id;
		this.data = data;
		this.desc = desc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "Domain [id=" + id + ", data=" + data + ", desc=" + desc + "]";
	}
}

class Entity { 
	private int id;
	private String description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Entity() {}
	public Entity(int id, String description) {
		super();
		this.id = id;
		this.description = description;
	}
	@Override
	public String toString() {
		return "Entity [id=" + id + ", description=" + description + "]";
	}
	
}