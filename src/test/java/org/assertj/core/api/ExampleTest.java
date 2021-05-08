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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Collection;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

public class ExampleTest {

  private final Example actual = new Example(Lists.list("A", "B"));
  private final Example expectedGetters = new Example(Lists.list("A"), "NOT_DUMMY");
  private final Example expectedFieldsAndGetters = new Example(Lists.list("A", "B"));

  @Test
  void comparingUsingGetters() {
    Assertions.assertThat(actual)
              .usingRecursiveComparison()
              .comparingUsingGetters()
              .isEqualTo(expectedGetters)
              .isEqualTo(expectedFieldsAndGetters);
  }

  @Test
  void comparingUsingFields() {
    Assertions.assertThat(actual)
              .usingRecursiveComparison()
              .comparingUsingFields()
              .isEqualTo(expectedFieldsAndGetters)
              .isNotEqualTo(expectedGetters);
  }

  public static class Example {
    private Collection<String> value;
    private String justField;

    public Example(Collection<String> value, String justField) {
      this.value = value;
      this.justField = justField;
    }

    public Example(Collection<String> value) {
      this(value, "DUMMY");
    }

    public String getValue() {
      if (value == null) {
        return null;
      }
      return value.iterator().next();
    }
  }

}
