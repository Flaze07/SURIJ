package io.github.flaze07;

import io.github.flaze07.net.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RouterPostTest {
	public static AtomicBoolean started = new AtomicBoolean();
    public static HttpServer server;

    @Test
    public void shouldGetHelloAndCode200WhenCalled() {
        RouterGetTest.started.set(false);
        var startServerThread = new Thread() {
            @Override
            public void run() {
                try {
                    server = new HttpServer(8000);
                    Router router = new Router();
                    router.post("/hello", new Handler() {
                        @Override
                        public Response handle(Request req) {
                            ResponseOptions options = new ResponseOptions.Builder()
                                                            .statusCode(200)
                                                            .body("hello")
                                                            .build();
                            return new Response(options);
                        }
                    });

                    server.addRouter("", router);
                    server.run(() -> {
                        RouterGetTest.started.set(true);
                    });
                } catch(IOException e) {
                    System.out.println(e);
                }
            }
        };
        startServerThread.start();
        while (RouterGetTest.started.get() == false) {
            //do nothing until the server started
        }

        try {
            URL url = new URL("http://localhost:8000/hello");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.connect();
            int responseCode = urlConn.getResponseCode();
            BufferedReader br = null;
            if (responseCode >= 200 && responseCode <= 399) {
                br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()));
            }

            String response = br.lines().collect(Collectors.joining());

            assertTrue(200 == responseCode);
            assertTrue("hello".equals(response));
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL Exception");
            assertTrue(false);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception");
            assertTrue(false);
        } catch (IOException e) {
            System.out.println("IO Exception");
            assertTrue(false);
        }

        server.stop(0);
    }
}