package io.github.flaze07.net;

public class Response
{
	private final ResponseOptions responseOptions;

	public Response(ResponseOptions responseOptions) {
		this.responseOptions = responseOptions;
	}

	public ResponseOptions getResponseOptions() {
		return this.responseOptions;
	}
}
