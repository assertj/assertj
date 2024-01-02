package org.assertj.core.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * CronExpression contains every unit of cron expression.
 * @author Neil Wang
 */
public enum CronUnit {
  SECONDS(0, 59, "^[*]$"),
  MINUTES(0, 59, "^[*]$"),
  HOURS(0, 23, "^[*]$"),
  DAY_OF_MONTH(1, 31, "^[*?L]$|^[0-9]W$|^[1-2][0-9]W$|^3[0-1]W$|^LW$"),
  MONTH(1, 12, "^[*]$",
    "JAN", "1", "jan", "1",
    "FEB", "2", "feb", "2",
    "MAR", "3", "mar", "3",
    "APR", "4", "apr", "4",
    "MAY", "5", "may", "5",
    "JUN", "6", "jun", "6",
    "JUL", "7", "jul", "7",
    "AUG", "8", "aug", "8",
    "SEP", "9", "sep", "9",
    "OCT", "10", "oct", "10",
    "NOV", "11", "nov", "11",
    "DEC", "12", "dec", "12"),
  DAY_OF_WEEK(0, 7, "^[*,?]$|^[0-7]#\\d+$|^[0-7]L$",
    "MON", "1", "mon", "1",
    "TUE", "2", "tue", "2",
    "WED", "3", "wed", "3",
    "THU", "4", "thu", "4",
    "FRI", "5", "fri", "5",
    "SAT", "6", "sat", "6",
    "SUN", "7", "sun", "7"
  ),
  YEAR(1970, 2099, "^[*,?]$");

  /**
   * min value of the unit.
   */
  private final int min;

  /**
   * max value of the unit.
   */
  private final int max;

  /**
   * wildcard pattern of the unit. If the value matches the pattern, it means the value is valid.
   */
  private final String wildcardPattern;

  /**
   * mappings of the unit. If the parsed value matches the key, it will be replaced by the value.
   * For example, if the parsed value is "MON-SUN", it will be replaced by "1-7".
   */
  private final Map<String, String> mappings;

  CronUnit(int min, int max, String wildcardPattern, String... mappings) {
    this.min = min;
    this.max = max;
    this.wildcardPattern = wildcardPattern;
    this.mappings = createMappings(mappings);
  }

  public int min() {
    return min;
  }

  public int max() {
    return max;
  }

  public String wildcardPattern() {
    return wildcardPattern;
  }

  public Map<String, String> mappings() {
    return mappings;
  }

  private Map<String, String> createMappings(String[] mappings) {
    Map<String, String> result = new HashMap<>();
    for (int i = 0; i < mappings.length; i += 2) {
      result.put(mappings[i], mappings[i + 1]);
    }
    return result;
  }
}
