package com.crossover.trial.weather.testIntegration;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.WeatherServer;
import com.jayway.restassured.RestAssured;

public class RestWeatherCollectorEndpointTestIntegration {

	private static WeatherServer server;
	
	@BeforeClass
	public static void beforeClass() {
		startServer();
		RestAssured.port = 9090;
		RestAssured.basePath = "/collect";
	}

	private static void startServer() {
		server = new WeatherServer();
		server.start();
	}
	
	@AfterClass
	public static void afterClass() {
		server.shutdown();
	}
	
	@Test
	public void ping() throws Exception {
		get("/ping")
			.then().assertThat()
				.statusCode(200)
				.body(equalTo("ready"));
	}

}
