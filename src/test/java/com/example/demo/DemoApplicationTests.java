package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	public void testLinkedHashMapMultiThreadedAccess() throws InterruptedException {
		// 작업 전 초기화
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		int numberOfThreads = 10;
		int numberOfEntriesPerThread = 10000;
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads * 2);
		
		// 시작 시간 기록
		long startTime = System.currentTimeMillis();

		// 작업 수행
		for (int i = 0; i < numberOfThreads; i++) {
			executor.submit(() -> {
				for (int j = 0; j < numberOfEntriesPerThread; j++) {
					String key = "key-" + UUID.randomUUID().toString();
					map.put(key, "value from thread " + Thread.currentThread().getName());
				}
				latch.countDown();
			});
		}
		
		// 모든 쓰레드의 작업이 종료될 때까지 대기한다.
		latch.await(1, TimeUnit.MINUTES);
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		
		// 종료 시간 기록
		long endTime = System.currentTimeMillis();
		
		// 결과
		int keySize = map.size();
		int expectedSize = numberOfThreads * numberOfEntriesPerThread;
		System.out.println("LinkedHashMap - keySize : " + keySize);
		System.out.println("LinkedHashMap - expectedSize : " + expectedSize);
		System.out.println("LinkedHashMap - duration : " + (endTime - startTime));
		assertTrue(keySize <= expectedSize, String.format("LinkedHashMap Size vs expectedSize", keySize, expectedSize));
	}
	
	@Test
	public void testConcurrentHashMapMultiThreadedAccess() throws InterruptedException {
		// 작업 전 초기화
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		int numberOfThreads = 10;
		int numberOfEntriesPerThread = 10000;
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads * 2);
		
		// 시작 시간 기록
		long startTime = System.currentTimeMillis();

		// 작업 수행
		for (int i = 0; i < numberOfThreads; i++) {
			executor.submit(() -> {
				for (int j = 0; j < numberOfEntriesPerThread; j++) {
					String key = "key-" + UUID.randomUUID().toString();
					map.put(key, "value from thread " + Thread.currentThread().getName());
				}
				latch.countDown();
			});
		}
		
		// 모든 쓰레드의 작업이 종료될 때까지 대기한다.
		latch.await(1, TimeUnit.MINUTES);
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		
		// 종료 시간 기록
		long endTime = System.currentTimeMillis();
		
		// 결과
		int keySize = map.size();
		int expectedSize = numberOfThreads * numberOfEntriesPerThread;
		System.out.println("ConcurrentHashMap - keySize : " + keySize);
		System.out.println("ConcurrentHashMap - expectedSize : " + expectedSize);
		System.out.println("ConcurrentHashMap - duration : " + (endTime - startTime));
		assertTrue(keySize == expectedSize, String.format("ConcurrentHashMap Size vs expectedSize", keySize, expectedSize));
	}

}
