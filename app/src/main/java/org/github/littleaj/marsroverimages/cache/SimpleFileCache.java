package org.github.littleaj.marsroverimages.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class SimpleFileCache implements SimpleCache {
  private static final Logger LOGGER = Logger.getLogger(SimpleFileCache.class.getName());

  static final String CACHE_SUBDIRECTORY = "mars-rover-images";

  private final File cacheDir;

  public SimpleFileCache(File baseDir) {
    if (baseDir == null) {
      throw new IllegalArgumentException("baseDir cannot be null");
    }
    cacheDir = new File(baseDir, CACHE_SUBDIRECTORY);
  }

  @Override
  public <T> void put(String key, T obj) {
    if (!cacheDir.exists()) {
      if (!cacheDir.mkdir()) {
        LOGGER.warning("could not create cache dir: " + cacheDir);
      }
    }

    File outFile = new File(cacheDir, key);
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile))) {
      LOGGER.info("Writing cache file: " + outFile);
      oos.writeObject(obj);
    } catch (IOException e) {
      LOGGER.warning("could not write to cache directory: " + e.getLocalizedMessage());
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(String key, Class<T> clazz) {
    File inFile = new File(cacheDir, key);
    if (inFile.exists()) {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFile))) {
        LOGGER.info("reading '" + key + "' from cache");
        return (T) ois.readObject();
      } catch (IOException e) {
        LOGGER.warning("could not read from cache directory: " + e.getLocalizedMessage());
      } catch (ClassNotFoundException e) {
        LOGGER.warning("invalid cache file: " + e.getLocalizedMessage());
      }
    }
    return null;
  }
}
