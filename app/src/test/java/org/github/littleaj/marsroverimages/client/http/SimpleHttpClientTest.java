package org.github.littleaj.marsroverimages.client.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.github.littleaj.marsroverimages.client.http.SimpleHttpClient.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleHttpClientTest {
  SimpleHttpClient client;

  @BeforeEach
  void setup() {
    client = new SimpleHttpClient();
  }

  @Test
  void existingPageReturns200() throws MalformedURLException, IOException {
    Response resp = client.get(new URL("https://httpstat.us/200"));
    assertThat(resp.getStatusCode()).isEqualTo(200);
    assertThat(resp.getStatusMessage()).isEqualTo("OK");
    assertThat(resp.getContent()).isNotEmpty();
  }

  @Test
  void nonexistantPageThrowsFileNotFound() throws MalformedURLException, IOException {
    assertThatThrownBy(() -> {
      client.get(new URL("https://httpstat.us/404"));
    }).isInstanceOf(FileNotFoundException.class);
  }

  @Test
  void errorPageThrowsIOException() throws MalformedURLException, IOException {
    assertThatThrownBy(() -> {
      client.get(new URL("https://httpstat.us/500"));
    }).isInstanceOf(IOException.class);
  }

}
