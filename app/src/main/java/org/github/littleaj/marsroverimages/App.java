package org.github.littleaj.marsroverimages;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.github.littleaj.marsroverimages.cache.SimpleFileCache;
import org.github.littleaj.marsroverimages.client.MarsImagesClient;
import org.github.littleaj.marsroverimages.client.http.SimpleHttpClient;
import org.github.littleaj.marsroverimages.config.AppConfig;
import org.github.littleaj.marsroverimages.json.GsonParser;
import org.github.littleaj.marsroverimages.json.JsonParser;
import org.github.littleaj.marsroverimages.params.AppParamsParser;
import org.github.littleaj.marsroverimages.service.MarsImagesService;

public class App {

  public static void main(String[] args) {
    configureLogging();

    final AppConfig config;
    try {
      config = AppConfig.resolver()
          .parameters(AppParamsParser.parse(args))
          .properties(loadProperties())
          .resolve();
    } catch (MissingApiKeyException e) {
      System.err.println(e.getLocalizedMessage());
      System.exit(1);
      return; // make the compiler happy
    }

    try {
      JsonParser jsonParser = new GsonParser();
      var service = new MarsImagesService(
          new MarsImagesClient(config.getApiKey(), new SimpleHttpClient(), jsonParser),
          new SimpleFileCache(new File(System.getProperty("java.io.tmpdir"))),
          config.getLimitPerDay()
      );
      var images = service.getImages(config.getStartDate(), config.getDays());
      System.out.println(jsonParser.serialize(images));
    } catch (MarsRoverImagesAppException e) {
      System.err.println("Exception occurred getting images: " + e.getLocalizedMessage());
      System.exit(2);
    }
  }

  private static Properties loadProperties() {
    try {
      var configProps = new Properties();
      configProps.load(App.class.getClassLoader().getResourceAsStream("config.properties"));
      return configProps;
    } catch (IOException e) {
      return null;
    }
  }

  private static void configureLogging() {
    try {
      LogManager.getLogManager()
          .readConfiguration(App.class.getClassLoader().getResourceAsStream("logging.properties"));
    } catch (SecurityException | IOException e) {
      // if error with config, turn off console logging
      Arrays.stream(Logger.getGlobal().getHandlers())
          .filter(h -> h instanceof ConsoleHandler)
          .forEach(h -> h.setLevel(Level.OFF));
    }
  }
}
