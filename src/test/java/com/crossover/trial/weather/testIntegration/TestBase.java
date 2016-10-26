package com.crossover.trial.weather.testIntegration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.crossover.trial.weather.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.WeatherServer;
import com.jayway.restassured.RestAssured;

public abstract class TestBase {

	private static WeatherServer server;
	
	@BeforeClass
	public static void beforeClass() {
		startServer();
		RestAssured.port = WeatherServer.PORT;
	}
	
	@Before
	public void before() {
		cleanup();
	}
	
	@AfterClass
	public static void afterClass() {
		server.shutdown();
	}
	
	private void cleanup() {
		RestWeatherQueryEndpoint.cleanup();
	}
	
	private static void startServer() {
		server = new WeatherServer();
		server.start();
	}
}