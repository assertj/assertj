package org.assertj.core.util.introspection;

import static java.lang.Character.isUpperCase;

import com.google.common.base.CaseFormat;

public class NormalizeStrategy {
  private NormalizeStrategy() {
  }

  public static String normalize(String name) {
    if (name.startsWith("_")) {
      return normalize(name.substring(1));  // avoid _last_name -> LastName
    }
    String camelCaseName = name.contains("_") ? CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name) : name;
    return normalizeAcronyms(camelCaseName);
  }

  private static String normalizeAcronyms(String name) {
    for (int i = 0; i < name.length(); i++) {
      if (!isUpperCase(name.charAt(i))) continue;
      int j = i + 1;
      while (j < name.length() && isUpperCase(name.charAt(j))) {
        j++;
      }
      return name.substring(0, i + 1)
              + name.substring(i + 1, j).toLowerCase()
              + normalizeAcronyms(name.substring(j));
    }
    return name;
  }
}
