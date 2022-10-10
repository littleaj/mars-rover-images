package org.github.littleaj.marsroverimages.config;

import java.time.LocalDate;

public class AppConfig {
  static final LocalDate DEFAULT_START_DATE = LocalDate.now();
  static final int DEFAULT_LIMIT_PER_DAY = 3;
  static final int DEFAULT_DAYS = 10;

  private String apiKey;
  private LocalDate startDate = DEFAULT_START_DATE;
  private int limitPerDay = DEFAULT_LIMIT_PER_DAY;
  private int days = DEFAULT_DAYS;

  AppConfig() {}

  public String getApiKey() {
    return apiKey;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public int getLimitPerDay() {
    return limitPerDay;
  }

  public int getDays() {
    return days;
  }

  void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  void setStartDate(LocalDate startDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("setStartDate requires nonnull parameter");
    }
    this.startDate = startDate;
  }

  void setLimitPerDay(int limitPerDay) {
    if (limitPerDay < 0) {
      throw new IllegalArgumentException("limitPerDay must be nonnegative");
    }
    this.limitPerDay = limitPerDay;
  }
  
  void setDays(int days) {
    if (days < 0) {
      throw new IllegalArgumentException("days must be nonnegative");
    }
    this.days = days;
  }

  public static AppConfigResolver resolver() {
    return new AppConfigResolver();
  }

  @Override
  public String toString() {
    return "AppConfig [apiKey=" + apiKey + ", startDate=" + startDate + ", limitPerDay=" + limitPerDay + "]";
  }

  

}
