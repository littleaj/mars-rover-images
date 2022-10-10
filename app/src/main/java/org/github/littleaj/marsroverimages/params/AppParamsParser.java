package org.github.littleaj.marsroverimages.params;

import java.util.regex.Pattern;

public class AppParamsParser {

  public static AppParams parse(String[] args) {
    String apikey = null;
    String startDate = null;
    String limitPerDay = null;
    String days = null;
  
    for (int i = 0; i < args.length; i++) {
      if (args[i].equalsIgnoreCase("--apikey")) {
        // get next arg for apikey, if available
        if (i + 1 < args.length) {
          apikey = args[++i];
        } // else hope there's a config file
      } else if (args[i].equalsIgnoreCase("--limitPerDay")) {
        if (i + 1 < args.length) {
          limitPerDay = args[++i];
        }
      } else if (args[i].equalsIgnoreCase("--days")) {
        if (i + 1 < args.length) {
          days = args[++i];
        }
      } else if (Pattern.matches("^\\d{4}-\\d\\d?-\\d\\d?$", args[i])) {
        startDate = args[i];
      }
    }
  
    return new AppParams(apikey, startDate, limitPerDay, days);
  }
  
}
