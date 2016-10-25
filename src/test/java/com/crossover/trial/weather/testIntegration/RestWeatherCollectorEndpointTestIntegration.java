package com.crossover.trial.weather.testIntegration;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class RestWeatherCollectorEndpointTestIntegration extends TestBase {

	@Test
	public void ping() throws Exception {
		get("/ping")
			.then().assertThat()
				.statusCode(200)
				.body(equalTo("ready"));
	}

	@BeforeClass
	public static void setupBasePath() {
		RestAssured.basePath = "/collect";
	}
}