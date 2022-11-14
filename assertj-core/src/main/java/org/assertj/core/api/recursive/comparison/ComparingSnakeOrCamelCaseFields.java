package org.assertj.core.api.recursive.comparison;

import static java.lang.Character.isUpperCase;

import org.assertj.core.util.introspection.CaseFormatUtils;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that transforms field names into camel case.
 */
public class ComparingSnakeOrCamelCaseFields extends ComparingNormalizedFields {

  public static final ComparingSnakeOrCamelCaseFields COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS = new ComparingSnakeOrCamelCaseFields();

  @Override
  public String normalizeFieldName(String name) {
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

  @Override
  public String getDescription() {
    return "comparing camel case and snake case fields";
  }

}
