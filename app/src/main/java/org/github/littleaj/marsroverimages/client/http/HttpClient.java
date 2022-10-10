package org.github.littleaj.marsroverimages.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface HttpClient {
  public interface Response {
    InputStream getContent();
    int getStatusCode();
    String getStatusMessage();
  }

  Response get(URL url) throws IOException;

}
