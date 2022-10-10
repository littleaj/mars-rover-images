package org.github.littleaj.marsroverimages.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.github.littleaj.marsroverimages.MarsRoverImagesAppException;
import org.github.littleaj.marsroverimages.client.http.HttpClient;
import org.github.littleaj.marsroverimages.client.http.SimpleHttpClient;
import org.github.littleaj.marsroverimages.json.JsonParser;
import org.github.littleaj.marsroverimages.schema.NasaPhotosResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MarsImagesClientTest {
  MarsImagesClient client;

  @Mock
  HttpClient mockHttpClient;

  @Mock
  JsonParser mockJsonParser;


  @BeforeEach
  void setup() throws IOException {
    client = new MarsImagesClient("test-key", mockHttpClient, mockJsonParser);
  }

  @Test
  void successfulResponseFromHttpClientParsesResponse() throws IOException {
    when(mockHttpClient.get(any())).thenReturn(new SimpleHttpClient.Response(200, null, null));
    when(mockJsonParser.deserialize(any(), any())).thenReturn(new NasaPhotosResponse());
    var respObj = client.getImagesForDay("test");
    assertThat(respObj).isNotNull();
    verify(mockHttpClient, times(1)).get(any());
    verify(mockJsonParser, times(1)).deserialize(any(), any());
  }

  @Test
  void exceptionFromHttpClientRethrows() throws Exception {
    when(mockHttpClient.get(any())).thenThrow(IOException.class);
    assertThatThrownBy(() -> {
      var respObj = client.getImagesForDay("test");
      fail("should not reach here: " + respObj);
    }).isInstanceOf(MarsRoverImagesAppException.class);
  }

  @Test
  void otherResponseFromHttpClientThrows() throws Exception {
    when(mockHttpClient.get(any())).thenReturn(new SimpleHttpClient.Response(300, null, null));
    assertThatThrownBy(() -> {
      var respObj = client.getImagesForDay("test");
      fail("should not reach here: " + respObj);
    }).isInstanceOf(MarsRoverImagesAppException.class);
  }

}
