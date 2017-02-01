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

import java.util.List;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that an object
 * has no null fields failed.
 *
 * @author Johannes Brodwall
 */
public class ShouldHaveNoNullFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting%n  <%s>%nto have a property or a field named <%s>";
  private static final String EXPECTED_SINGLE = "%nExpecting%n  <%s>%nnot to have any null property or field, but <%s> was null.%n";
  private static final String COMPARISON = "Check was performed on all fields/properties";
  private static final String EXCLUDING = COMPARISON + " except: <%s>";

  public ShouldHaveNoNullFields(Object actual, List<String> rejectedFields, List<String> ignoredFields) {
    super(EXPECTED_MULTIPLE + EXCLUDING, actual, rejectedFields, ignoredFields);
  }

  public ShouldHaveNoNullFields(Object actual, List<String> rejectedFields) {
    super(EXPECTED_MULTIPLE + COMPARISON, actual, rejectedFields);
  }

  public ShouldHaveNoNullFields(Object actual, String rejectedField) {
    super(EXPECTED_SINGLE + COMPARISON, actual, rejectedField);
  }

  public ShouldHaveNoNullFields(Object actual, String rejectedField, List<String> ignoredFields) {
    super(EXPECTED_SINGLE + EXCLUDING, actual, rejectedField, ignoredFields);
  }

  public static ShouldHaveNoNullFields shouldHaveNoNullFieldsExcept(Object actual, List<String> rejectedFields,
                                                                    List<String> ignoredFields) {
    if (rejectedFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldHaveNoNullFields(actual, rejectedFields.get(0));
      }
      return new ShouldHaveNoNullFields(actual, rejectedFields.get(0), ignoredFields);
    }
    if (ignoredFields.isEmpty()) {
      return new ShouldHaveNoNullFields(actual, rejectedFields);
    }
    return new ShouldHaveNoNullFields(actual, rejectedFields, ignoredFields);
  }

}
