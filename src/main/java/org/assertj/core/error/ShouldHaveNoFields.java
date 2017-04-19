/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import java.util.Set;

public class ShouldHaveNoFields extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveNoPublicFields(Class<?> actual, Set<String> fields) {
    return new ShouldHaveNoFields(actual, fields, true, false);
  }

  public static ErrorMessageFactory shouldHaveNoDeclaredFields(Class<?> actual, Set<String> fields) {
    return new ShouldHaveNoFields(actual, fields, false, true);
  }

  private ShouldHaveNoFields(Class<?> actual, Set<String> fields, boolean publik, boolean declared) {
    super("%nExpecting%n" +
          "  <%s>%n" +
          "not to have any " + fieldDescription(publik, declared) + " fields but it has:%n" +
          "  <%s>", actual, fields);
  }

  private static String fieldDescription(boolean publik, boolean declared) {
    if (publik) {
      return declared ? "public declared" : "public";
    }
    return declared ? "declared" : "";
  }
}
