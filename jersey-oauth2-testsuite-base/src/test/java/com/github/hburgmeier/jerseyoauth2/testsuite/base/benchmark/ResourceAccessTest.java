package com.github.hburgmeier.jerseyoauth2.testsuite.base.benchmark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.github.hburgmeier.jerseyoauth2.client.scribe.OAuth2Token;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.PerformanceTest;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.SampleEntity;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientException;

public class ResourceAccessTest extends PerformanceTest {

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();
	
	private static long testCount = 0l;	
	
	@BenchmarkOptions(benchmarkRounds=200, concurrency=BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
	@Test
	public void testResourceAccessWithRefresh() throws ClientException
	{
		testCount++;
		if (testCount % 50 == 0 && testCount>0)
			token = client.refreshToken((OAuth2Token)token);
		SampleEntity entity = client.retrieveEntitySample1(token);
		assertNotNull(entity);
		assertEquals("manager", entity.getUsername());
		assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}	
	
}
