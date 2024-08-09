package com.github.flaze07.net;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class Router
{
	public class MethodRoute
	{
		private Optional<Handler> getMethod;
		private Optional<Handler> postMethod;
		private Optional<Handler> putMethod;
		private Optional<Handler> deleteMethod;

		public MethodRoute() {
			getMethod = Optional.empty();
			postMethod = Optional.empty();
			putMethod = Optional.empty();
			deleteMethod = Optional.empty();
		}

		public void setGet(Handler handler) {
			getMethod = Optional.of(handler);
		}

		public void setPost(Handler handler) {
			postMethod = Optional.of(handler);
		}

		public void setPut(Handler handler) {
			putMethod = Optional.of(handler);
		}

		public void setDelete(Handler handler) {
			deleteMethod = Optional.of(handler);
		}

		public Optional<Handler> getGet() {
			return getMethod;
		}

		public Optional<Handler> getPost() {
			return postMethod;
		}

		public Optional<Handler> getPut() {
			return putMethod;
		}

		public Optional<Handler> getDelete() {
			return deleteMethod;
		}
	}

	/**
	 * String is to denote the path
	 */
	private Map<String, MethodRoute> routes;
	public Router() {
		routes = new HashMap<String, MethodRoute>();
	}

	public Map<String, MethodRoute> getRoutes(HttpServer.RouterAccessor accessor) {
		Objects.requireNonNull(accessor);
		return routes;
	}

	public void get(String path, Handler handler) {
		if (!routes.containsKey(path)) {
			routes.put(path, new MethodRoute());
		}
		routes.get(path).setGet(handler);
	}

	public void post(String path, Handler handler) {
		if (!routes.containsKey(path)) {
			routes.put(path, new MethodRoute());
		}
		routes.get(path).setPost(handler);
	}

	public void put(String path, Handler handler) {
		if (!routes.containsKey(path)) {
			routes.put(path, new MethodRoute());
		}
		routes.get(path).setPut(handler);
	}

	public void delete(String path, Handler handler) {
		if (!routes.containsKey(path)) {
			routes.put(path, new MethodRoute());
		}
		routes.get(path).setDelete(handler);
	}
}