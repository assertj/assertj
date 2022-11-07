package org.assertj.core.util.introspection;

import static java.lang.Character.isUpperCase;

public class NormalizeStrategy {
  private NormalizeStrategy() {
  }

  public static String normalize(String name) {
    String camelCaseName = CaseFormatUtils.toCamelCase(name);
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
