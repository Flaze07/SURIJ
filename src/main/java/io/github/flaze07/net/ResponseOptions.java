package io.github.flaze07.net;

import java.util.HashMap;
import java.util.Map;

public class ResponseOptions {
    private final Map<String, String> headers;
    private final int statusCode;
    private final String body;

    public ResponseOptions(Map<String, String> headers, int statusCode, String body) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private Map<String, String> headers;
        private int statusCode;
        private String body;

        public Builder() {
            headers = new HashMap<String, String>();
            statusCode = 200;
            body = "";
        }

        public Builder headers(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public ResponseOptions build() {
            return new ResponseOptions(headers, statusCode, body);
        }
    }
}