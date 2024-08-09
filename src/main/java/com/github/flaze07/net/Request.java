package com.github.flaze07.net;

import java.util.Map;
import java.util.Optional;

public class Request
{
	private final String path;
	private final Map<String, String> queries;

	public Request(Builder builder) {
		this.path = builder.path;
		this.queries = builder.queries;
	}

	public String getpath() {
		return path;
	}

	public Optional<String> getQuery(String key) {
		if(!queries.containsKey(key)) {
			return Optional.empty();
		} else {
			return Optional.of(queries.get(key));
		}
	}

	public static class Builder 
	{
		private String path;
		private Map<String, String> queries;

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder queries(Map<String, String> queries) {
			this.queries = queries;
			return this;
		}

		public Request build() {
			return new Request(this);
		}
	}
}