package org.github.littleaj.marsroverimages.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.github.littleaj.marsroverimages.MarsRoverImagesAppException;
import org.github.littleaj.marsroverimages.client.http.HttpClient;
import org.github.littleaj.marsroverimages.client.http.HttpClient.Response;
import org.github.littleaj.marsroverimages.json.JsonParser;
import org.github.littleaj.marsroverimages.schema.NasaPhotosResponse;

public class MarsImagesClient {
  private static Logger LOGGER = Logger.getLogger(MarsImagesClient.class.getName());

  private final String apikey;
  private final HttpClient httpClient;
  private final JsonParser jsonParser;

  public MarsImagesClient(String apikey, HttpClient httpClient, JsonParser jsonParser) {
    if (apikey == null || apikey.isBlank()) {
      throw new IllegalArgumentException("apikey must be non-blank");
    }
    this.apikey = apikey;
    this.httpClient = httpClient;
    this.jsonParser = jsonParser;
  }

  public NasaPhotosResponse getImagesForDay(String date) {
    final URL url = buildUrl(date);
    
    LOGGER.info("Querying " + url.toString());
    final Response response;
    try {
      response = httpClient.get(url);
    } catch (IOException e) {
      throw new MarsRoverImagesAppException("error communicating with nasa api", e);
    }

    if (response.getStatusCode() == 200) {
      return jsonParser.deserialize(response.getContent(), NasaPhotosResponse.class);
    } else {
      throw new MarsRoverImagesAppException("error response from nasa api: " + response.getStatusCode() + " " + response.getStatusMessage());
    }
  }

  URL buildUrl(String date) {
    try {
      return new URL(String.format(
          "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?camera=NAVCAM&earth_date=%s&api_key=%s",
          date, this.apikey)
      );
    } catch (MalformedURLException e) {
      throw new MarsRoverImagesAppException("malformed url", e);
    }
  }
}
