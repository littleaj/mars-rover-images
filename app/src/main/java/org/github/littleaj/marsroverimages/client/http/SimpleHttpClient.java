package org.github.littleaj.marsroverimages.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient implements HttpClient {

  public static class Response implements HttpClient.Response {

    private final int statusCode;
    private final String statusMessage;
    private final InputStream content;

    public Response(int statusCode, String statusMessage, InputStream content) {
      this.statusCode = statusCode;
      this.statusMessage = statusMessage;
      this.content = content;
    }

    @Override
    public InputStream getContent() {
      return content;
    }

    @Override
    public int getStatusCode() {
      return statusCode;
    }

    @Override
    public String getStatusMessage() {
      return statusMessage;
    }

  }

  @Override
  public Response get(URL url) throws IOException {
    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    return new Response(conn.getResponseCode(), conn.getResponseMessage(), conn.getInputStream());
  }
  
}
