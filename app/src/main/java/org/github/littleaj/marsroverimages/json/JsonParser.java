package org.github.littleaj.marsroverimages.json;

import java.io.InputStream;

public interface JsonParser {
  <T> T deserialize(InputStream in, Class<T> clazz);
  String serialize(Object obj);
}
