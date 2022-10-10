package org.github.littleaj.marsroverimages.json;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class GsonParser implements JsonParser {

  private final Gson gson = new GsonBuilder()
      .setPrettyPrinting()
      .create();

  @Override
  public <T> T deserialize(InputStream in, Class<T> clazz) {
    return gson.fromJson(new JsonReader(new InputStreamReader(in)), clazz);
  }

  @Override
  public String serialize(Object obj) {
    return gson.toJson(obj);
  }
  
}
