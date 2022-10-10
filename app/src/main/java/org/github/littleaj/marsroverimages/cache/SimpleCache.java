package org.github.littleaj.marsroverimages.cache;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface SimpleCache {
  <T> void put(String key, T obj);

  <T> T get(String key, Class<T> clazz);

  default <T> T get(String key, Class<T> clazz, Supplier<T> supplier, Predicate<T> condition) {
    T result = get(key, clazz);
    if (result == null) {
      result = supplier.get();
      if (condition.test(result)) {
        put(key, result);
      }
    }
    return result;
  }
}
