package io.github.flaze07;

import io.github.flaze07.net.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RouteTest {
	public static AtomicBoolean started = new AtomicBoolean(false);
	public static HttpServer server;

	@Test
	public void ShouldGetHelloAnd200WhenCalled() {
		started.set(false);
		var startServerThread = new Thread() {
			@Override
			public void run() {
				try {
					server = new HttpServer(8000);
					Router router = new Router();
					router.get("", new Handler() {
						@Override
						public Response handle(Request req) {
							return new Response(new ResponseOptions.Builder()
														.statusCode(200)
														.body("Hello")
														.build());
						}
					});
					server.addRouter("/api", router);
					server.run(() -> {
						started.set(true);
					});
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		};

		startServerThread.start();

		while (started.get() == false) {
			//do nothing
		}

		try {
			URL url = new URL("http://localhost:8000/api");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			var responseCode = urlConn.getResponseCode();

			BufferedReader br = null;
			if (responseCode >= 200 && responseCode <= 399) {
				br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()));				
			}

			var resp = br.lines().collect(Collectors.joining());

			assertTrue("Hello".equals(resp));
			assertTrue(200 == responseCode);
		} catch (Exception e) {
			System.out.println(e);
		}

		server.stop(0);
	}

	@Test
	public void ShouldGetHelloAnd200WhenCalled2() {
		started.set(false);
		var startServerThread = new Thread() {
			@Override
			public void run() {
				try {
					server = new HttpServer(8000);
					Router router = new Router();
					router.get("/hello", new Handler() {
						@Override
						public Response handle(Request req) {
							return new Response(new ResponseOptions.Builder()
														.statusCode(200)
														.body("Hello")
														.build());
						}
					});
					server.addRouter("/api", router);
					server.run(() -> {
						started.set(true);
					});
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		};

		startServerThread.start();

		while (started.get() == false) {
			//do nothing
		}

		try {
			URL url = new URL("http://localhost:8000/api/hello");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			var responseCode = urlConn.getResponseCode();

			BufferedReader br = null;
			if (responseCode >= 200 && responseCode <= 399) {
				br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()));				
			}

			var resp = br.lines().collect(Collectors.joining());

			assertTrue("Hello".equals(resp));
			assertTrue(200 == responseCode);
		} catch (Exception e) {
			System.out.println(e);
		}

		server.stop(0);
	}
}