/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.Character.isUpperCase;
import static org.assertj.core.util.introspection.CaseFormatUtils.toCamelCase;

/**
 * A {@link RecursiveComparisonIntrospectionStrategy} that transforms snake case field names into camel case.
 * <p>
 * The normalization also normalize uppercase acronyms by keeping only the first acronym letter uppercase so that
 * {@code profile_url} and {@code profileURL} can be matched, both being normalized to {@code profileUrl}.
 *
 * @since 3.24.0
 */
public class ComparingSnakeOrCamelCaseFields extends ComparingNormalizedFields {

  public static final ComparingSnakeOrCamelCaseFields COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS = new ComparingSnakeOrCamelCaseFields();

  /**
   * Transforms snake case field names into camel case (leave camel case fields as is).
   * <p>
   * For example, this allows to compare {@code Person} object with camel case fields like {@code firstName} to a
   * {@code PersonDto} object with snake case fields like {@code first_name}.
   *
   * @param name the field name to normalize
   * @return camel case version of the field name
   */
  @Override
  public String normalizeFieldName(String name) {
    String camelCaseName = toCamelCase(name);
    return normalizeAcronyms(camelCaseName);
  }

  /**
   * Normalizes uppercase acronyms by keeping only the first acronym letter uppercase, ex: {@code normalizeAcronyms("URl")} gives
   * {@code "Url"}
   *
   * @param name the name to normalize
   * @return the normalized name
   */
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
