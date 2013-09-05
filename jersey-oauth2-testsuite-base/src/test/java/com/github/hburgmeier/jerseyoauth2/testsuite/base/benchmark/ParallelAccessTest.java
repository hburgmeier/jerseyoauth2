package com.github.hburgmeier.jerseyoauth2.testsuite.base.benchmark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.PerformanceTest;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.SampleEntity;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientException;

public class ParallelAccessTest extends PerformanceTest {

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();
	
	@Ignore
	@BenchmarkOptions(benchmarkRounds=200, concurrency=BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
	@Test
	public void testParallelResourceAccess() throws ClientException
	{
		SampleEntity entity = client.retrieveEntitySample1(token);
		assertNotNull(entity);
		assertEquals("manager", entity.getUsername());
		assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}	
}
