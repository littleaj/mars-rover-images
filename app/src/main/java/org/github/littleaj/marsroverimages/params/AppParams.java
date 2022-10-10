package org.github.littleaj.marsroverimages.params;

/**
 * Application CLI options
 */
public class AppParams {
  private final String apikey;
  private final String startDate;
  private final String limitPerDay;
  private final String days;

  AppParams(String apikey, String startDate, String limitPerDay, String days) {
    this.apikey = apikey;
    this.startDate = startDate;
    this.limitPerDay = limitPerDay;
    this.days = days;
  }

  public String getApiKey() {
    return apikey;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getLimitPerDay() {
    return limitPerDay;
  }

  public String getDays() {
    return days;
  }
}
