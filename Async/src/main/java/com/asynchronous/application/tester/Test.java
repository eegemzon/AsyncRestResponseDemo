package com.asynchronous.application.tester;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Test {

	public static Domain getValue(int val) throws IllegalArgumentException {
		//throw new IllegalArgumentException("Invalid value " + val);
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Domain(false, 10);
	}
	
	public static Domain getAnotherValue(int val) throws IllegalArgumentException {
		//throw new IllegalArgumentException("Invalid another value " + val);
		return new Domain(false, 20);
	}
	
	public static Domain getDefaultValue() {
		System.out.println("Returning default value");
		return new Domain(true, 1);
	}
	
	public static Domain getAnotherDefaultValue() {
		System.out.println("Returning another default value");
		return new Domain(true, 2);
	}
	
	public static void main(String[] args) throws Exception {
		//CompletableFuture<Integer> int1Fut = CompletableFuture.supplyAsync(() -> getValue(1)).exceptionally((ex) -> getDefaultValue());
		CompletableFuture<Domain> int1Fut = CompletableFuture.supplyAsync(() -> getValue(1))
				.orTimeout(5, TimeUnit.SECONDS)
				.handle((value, ex) -> {
					if(ex == null) {
						System.out.println("no exception occurred");
						return value;
					}
					return getDefaultValue();
				});
		CompletableFuture<Domain> int2Fut = CompletableFuture.supplyAsync(() -> getAnotherValue(1))
				.handle((value, ex) -> {
					if(ex == null) {
						System.out.println("no exception occurred");
						return value;
					}
					return getAnotherDefaultValue();
				});
		try {
			CompletableFuture.allOf(int1Fut, int2Fut).join();
			if(int1Fut.get().isDefaultValue() && int2Fut.get().isDefaultValue()) {
				throw new Exception("Cannot complete process, both failed");
			}
			System.out.println("int1Fut == " + int1Fut.get());
			System.out.println("int2Fut == " + int2Fut.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}		
}

class Domain {
	private boolean defaultValue;
	private Integer value;
	
	public Domain(boolean defaultValue, Integer value) {
		super();
		this.defaultValue = defaultValue;
		this.value = value;
	}
	public boolean isDefaultValue() {
		return defaultValue;
	}
	public Integer getValue() {
		return value;
	}
	@Override
	public String toString() {
		return "Domain [defaultValue=" + defaultValue + ", value=" + value + "]";
	}
}
