package com.crossover.trial.weather.rest;

import static com.jayway.restassured.RestAssured.registerParser;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.crossover.trial.weather.WeatherServer;
import com.crossover.trial.weather.repository.AirportRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;

public abstract class RestTestBase {

	private static WeatherServer server;
	
	@BeforeClass
	public static void beforeClass() {
		startServer();
		RestAssured.port = WeatherServer.PORT;
		RestAssured.defaultParser = Parser.JSON;
		registerParser("text/plain", Parser.JSON);
	}
	
	@Before
	public void before() {
		WeatherQueryEndpointImpl.init();
		AirportRepository.init();
	}
	
	@AfterClass
	public static void afterClass() {
		server.shutdown();
	}
	
	private static void startServer() {
		server = new WeatherServer();
		server.start();
	}
}