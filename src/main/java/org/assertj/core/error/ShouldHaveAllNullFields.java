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

import java.util.List;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that an object
 * has null fields failed.
 *
 * @author Vladimir Chernikov
 */
public class ShouldHaveAllNullFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting%n  <%s>%nto only have null properties or fields but these were not null:%n <%s>.%n";
  private static final String EXPECTED_SINGLE = "%nExpecting%n  <%s>%nto only have null property or field, but <%s> was not null.%n";
  private static final String COMPARISON = "Check was performed on all fields/properties";
  private static final String EXCLUDING = COMPARISON + " except: <%s>.";
  private static final String DOT = ".";

  public ShouldHaveAllNullFields(Object actual, List<String> nonNullFields, List<String> ignoredFields) {
    super(EXPECTED_MULTIPLE + EXCLUDING, actual, nonNullFields, ignoredFields);
  }

  public ShouldHaveAllNullFields(Object actual, List<String> nonNullFields) {
    super(EXPECTED_MULTIPLE + COMPARISON + DOT, actual, nonNullFields);
  }

  public ShouldHaveAllNullFields(Object actual, String nonNullField) {
    super(EXPECTED_SINGLE + COMPARISON + DOT, actual, nonNullField);
  }

  public ShouldHaveAllNullFields(Object actual, String nonNullField, List<String> ignoredFields) {
    super(EXPECTED_SINGLE + EXCLUDING, actual, nonNullField, ignoredFields);
  }

  public static ShouldHaveAllNullFields shouldHaveAllNullFields(Object actual, List<String> nonNullFields,
                                                                List<String> ignoredFields) {
    if (nonNullFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldHaveAllNullFields(actual, nonNullFields.get(0));
      }
      return new ShouldHaveAllNullFields(actual, nonNullFields.get(0), ignoredFields);
    }
    return ignoredFields.isEmpty()
        ? new ShouldHaveAllNullFields(actual, nonNullFields)
        : new ShouldHaveAllNullFields(actual, nonNullFields, ignoredFields);
  }
}
