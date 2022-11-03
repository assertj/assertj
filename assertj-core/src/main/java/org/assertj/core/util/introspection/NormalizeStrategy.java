package org.assertj.core.util.introspection;

import com.google.common.base.CaseFormat;

public class NormalizeStrategy {
  private NormalizeStrategy() {
  }

  public static String normalize(String name) {
    if (!name.contains("_")) {
      return name;
    }
    if (name.startsWith("_")) {
      return normalize(name.substring(1));  // avoid _last_name -> LastName
    }
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
  }
}
