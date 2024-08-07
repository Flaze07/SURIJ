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
import java.lang.reflect.*;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RouterGetTest {
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
                    router.get("/hello", new Handler() {
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
            urlConn.setRequestMethod("GET");
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

        try {
            Class<?> obj = RouterGetTest.server.getClass();
            Field field1 = obj.getDeclaredField("internalServer");
            field1.setAccessible(true);
            var internalServer = (com.sun.net.httpserver.HttpServer) field1.get(RouterGetTest.server);
            internalServer.stop(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void ShouldGetQueryIdAndCode200WhenCalled() {
        RouterGetTest.started.set(false);
        var startServerThread = new Thread() {
            @Override
            public void run() {
                try {
                    server = new HttpServer(8000);
                    Router router = new Router();
                    router.get("/hello", new Handler() {
                        @Override
                        public Response handle(Request req) {
                            var id = req.getQuery("id");
                            String out = "";
                            if (id.isPresent()) {
                                out = id.get();
                            }
                            ResponseOptions options = new ResponseOptions.Builder()
                                                            .statusCode(200)
                                                            .body(out)
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
            var id = "fxgt-3012-sdsa-ppee";
            URL url = new URL(String.format("http://localhost:8000/hello?id=%s", id));
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
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
            assertTrue(id.equals(response));
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

        try {
            Class<?> obj = RouterGetTest.server.getClass();
            Field field1 = obj.getDeclaredField("internalServer");
            field1.setAccessible(true);
            var internalServer = (com.sun.net.httpserver.HttpServer) field1.get(RouterGetTest.server);
            internalServer.stop(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void shouldGetErrorAndCode400WhenCalled() {
        RouterGetTest.started.set(false);
        var startServerThread = new Thread() {
            @Override
            public void run() {
                try {
                    server = new HttpServer(8000);
                    Router router = new Router();
                    router.get("/hello", new Handler() {
                        @Override
                        public Response handle(Request req) {
                            var out = "";
                            var idQuery = req.getQuery("id");
                            if (idQuery.isEmpty()) {
                                ResponseOptions options = new ResponseOptions.Builder()
                                                                .statusCode(400)
                                                                .body("Id query missing")
                                                                .build();
                                return new Response(options);
                            } 

                            out = idQuery.get();

                            ResponseOptions options = new ResponseOptions.Builder()
                                                            .statusCode(200)
                                                            .body(out)
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
            urlConn.setRequestMethod("GET");
            urlConn.connect();
            int responseCode = urlConn.getResponseCode();
            BufferedReader br = null;
            if (responseCode >= 200 && responseCode <= 399) {
                br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()));
            }

            String response = br.lines().collect(Collectors.joining());

            assertTrue(400 == responseCode);
            assertTrue("Id query missing".equals(response));
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

        try {
            Class<?> obj = RouterGetTest.server.getClass();
            Field field1 = obj.getDeclaredField("internalServer");
            field1.setAccessible(true);
            var internalServer = (com.sun.net.httpserver.HttpServer) field1.get(RouterGetTest.server);
            internalServer.stop(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
