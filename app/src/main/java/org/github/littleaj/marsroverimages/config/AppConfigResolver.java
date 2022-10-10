package org.github.littleaj.marsroverimages.config;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Properties;
import java.util.logging.Logger;

import org.github.littleaj.marsroverimages.MissingApiKeyException;
import org.github.littleaj.marsroverimages.params.AppParams;

public class AppConfigResolver {
  private static final Logger LOGGER = Logger.getLogger(AppConfigResolver.class.getName());

  private Properties props;
  private AppParams params;

  AppConfigResolver() {}

  public AppConfigResolver properties(Properties props) {
    this.props = props;
    return this;
  }

  public AppConfigResolver parameters(AppParams params) {
    this.params = params;
    return this;
  }

  public AppConfig resolve() throws MissingApiKeyException {
    AppConfig result = new AppConfig();
    resolveProps(result);
    resolveParams(result);

    if (result.getApiKey() == null) {
      throw new MissingApiKeyException("No apikey given.");
    }

    return result;
  }

  static void resolveLimit(String source, AppConfig result, String limitPerDay) {
    if (isNotBlank(limitPerDay)) {
      try {
        result.setLimitPerDay(Integer.parseInt(limitPerDay));
      } catch (NumberFormatException e) {
        LOGGER.warning("Could not parse limitPerDay " + source + " '" + limitPerDay + "': " + e.getLocalizedMessage());
      }
    }
  }
  
  static void resolveStartDate(String source, AppConfig result, String startDate) {
    if (isNotBlank(startDate)) {
      try {
        result.setStartDate(LocalDate.parse(startDate));
      } catch (DateTimeParseException e) {
        LOGGER.warning("Could not parse startDate " + source + " '" + startDate + "': " + e.getLocalizedMessage());
      }
    }
  }
  
  static void resolveApiKey(AppConfig result, String apikey) {
    if (isNotBlank(apikey)) {
      result.setApiKey(apikey);
    }
  }
  
  static void resolveDays(String source, AppConfig result, String days) {
    if (isNotBlank(days)) {
      try {
        result.setDays(Integer.parseInt(days));
      } catch (NumberFormatException e) {
        LOGGER.warning("Could not parse days " + source + " '" + days + "': " + e.getLocalizedMessage());
      }
    }
  }

  private void resolveParams(AppConfig result) {
    if (params != null) {
      resolveApiKey(result, params.getApiKey());
      resolveStartDate("parameter", result, params.getStartDate());
      resolveLimit("parameter", result, params.getLimitPerDay());
      resolveDays("parameter", result, params.getDays());
    }
  }

  private void resolveProps(AppConfig result) {
    if (props != null) {
      resolveApiKey(result, props.getProperty("apikey"));
      resolveStartDate("property", result, props.getProperty("startDate"));
      resolveLimit("property", result, props.getProperty("limitPerDay"));
      resolveDays("property", result, props.getProperty("days"));
    }
  }

  static boolean isNotBlank(String startDateProp) {
    return startDateProp != null && !startDateProp.isBlank();
  }
}
