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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Creates an error message indicating that an assertion that verifies that a class has a given field/property.
 *
 * @author Victor Wang
 */
public class ShouldHaveOnlyFieldsOrProperties extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveOnlyFieldsOrProperties}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param names expected name list of field for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveOnlyFieldsOrProperties(Object actual, String... names) {
    return new ShouldHaveOnlyFieldsOrProperties(actual, names);
  }

  private ShouldHaveOnlyFieldsOrProperties(Object actual, String... names) {
    super("%nExpecting%n  <%s>%nto have  properties and a fields named <%s>", actual, Arrays.stream(names).sorted().map(Object::toString).collect(Collectors.joining(", ")));
  }
}
