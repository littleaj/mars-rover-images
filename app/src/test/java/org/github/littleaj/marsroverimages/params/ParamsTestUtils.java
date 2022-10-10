package org.github.littleaj.marsroverimages.params;

public class ParamsTestUtils {
  public static AppParams newParams(String apikey, String date, String limit, String days) {
    return new AppParams(apikey, date, limit, days);
  }
}
