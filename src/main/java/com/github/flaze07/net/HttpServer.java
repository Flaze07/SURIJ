package com.github.flaze07.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.lang.Runnable;


import io.github.flaze07.util.StringFunctions;

public class HttpServer 
{
	public static final class RouterAccessor 
	{
		private RouterAccessor() {}
	}
	private static final RouterAccessor routerAccessor = new RouterAccessor(); 

	private Map<String, Router> routers; 
	private int port;
	private com.sun.net.httpserver.HttpServer internalServer;

	public HttpServer(int port) {
		this.port = port;
		this.routers = new HashMap<String, Router>();
	}

	public void addRouter(String path, Router router) {
		routers.put(path, router);
	}

	public void run(Runnable cb) throws IOException {
		internalServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);

		for (Map.Entry<String, Router> routersEntry: routers.entrySet()) {
			var prefixPath = routersEntry.getKey();
			var router = routersEntry.getValue();

			for (var routeEntry: router.getRoutes(routerAccessor).entrySet()) {
				var path = routeEntry.getKey();
				var methodRoute = routeEntry.getValue();
				
				var getMethod = methodRoute.getGet(); 
				var postMethod = methodRoute.getPost();
				var putMethod = methodRoute.getPut();
				var deleteMethod = methodRoute.getDelete();

				var finalPath = prefixPath + path;

				internalServer.createContext(finalPath, (exchange) -> {
					var requestBuilder = new Request.Builder();
					requestBuilder.path(finalPath);
					Optional<Response> response = Optional.empty();
					if ("GET".equals(exchange.getRequestMethod())) {
						if (getMethod.isPresent()) {
							var queries = exchange.getRequestURI().getRawQuery();
							requestBuilder.queries(StringFunctions.splitQuery(queries));
							var request = requestBuilder.build();

							response = Optional.of(getMethod.get().handle(request));
						} else {
							exchange.sendResponseHeaders(405, -1);
						}
					} else if ("POST".equals(exchange.getRequestMethod())) {
						if (postMethod.isPresent()) {
							var request = requestBuilder.build();

							response = Optional.of(postMethod.get().handle(request));
						} else {
							exchange.sendResponseHeaders(405, -1);
						}
					} else if ("PUT".equals(exchange.getRequestMethod())) {
						if (putMethod.isPresent()) {
							var request = requestBuilder.build();

							response = Optional.of(putMethod.get().handle(request));
						} else {
							exchange.sendResponseHeaders(405, -1);
						}
					} else if ("DELETE".equals(exchange.getRequestMethod())) {
						if (deleteMethod.isPresent()) {
							var request = requestBuilder.build();

							response = Optional.of(deleteMethod.get().handle(request));
						} else {
							exchange.sendResponseHeaders(405, -1);
						}
					}

					if (response.isPresent()) {
						var resp = response.get();
						var respOptions = resp.getResponseOptions();
						var headers = respOptions.getHeaders();
						var statusCode = respOptions.getStatusCode();
						var body = respOptions.getBody();

						for(var header: headers.entrySet()) {
							var key = header.getKey();
							var value = header.getValue();

							exchange.getResponseHeaders().set(key, value);
						}

						exchange.sendResponseHeaders(statusCode, body.getBytes().length);
						var output = exchange.getResponseBody();
						output.write(body.getBytes());
						output.flush();
					}

					exchange.close();
				});
			} 
		}

		internalServer.setExecutor(null);
		if (cb != null) {
			cb.run();
		}
		internalServer.start();
	}

	public void stop(int delay) {
		internalServer.stop(delay);
	}
}