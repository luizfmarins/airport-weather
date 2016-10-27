package com.crossover.trial.weather.main;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.crossover.trial.weather.rest.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.rest.RestWeatherQueryEndpoint;


/**
 * This main method will be use by the automated functional grader. You shouldn't move this class or remove the
 * main method. You may change the implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

	public static final int PORT = 9090;
    private static final String BASE_URL = "http://localhost:" + PORT + "/";

    private HttpServer server;

    public static void main(String[] args) {
    	WeatherServer weatherServer = new WeatherServer();
        weatherServer.start();
        weatherServer.blocksUntilTheProcessIsTerminated();
        weatherServer.shutdown();
    }

	public void start() {
		try {
            System.out.println("Starting Weather App local testing server: " + BASE_URL);

            final ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.register(RestWeatherCollectorEndpoint.class);
            resourceConfig.register(RestWeatherQueryEndpoint.class);

            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), resourceConfig, false);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdownNow();
            }));

            HttpServerProbe probe = new HttpServerProbe.Adapter() {
                public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection, Request request) {
                    System.out.println(request.getRequestURI());
                }
            };
            server.getServerConfiguration().getMonitoringConfig().getWebServerConfig().addProbes(probe);

            // the autograder waits for this output before running automated tests, please don't remove it
            server.start();
            System.out.println(format("Weather Server started.\n url=%s\n", BASE_URL));
        } catch (IOException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	public void shutdown() {
		server.shutdown();
	}

	private void blocksUntilTheProcessIsTerminated() {
		try {
			Thread.currentThread().join();
		} catch (InterruptedException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
