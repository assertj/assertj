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
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are lenient equal by
 * ignoring fields failed.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ShouldBeEqualToIgnoringFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting values:%n  <%s>%nin fields:%n  <%s>%nbut were:%n  <%s>%nin <%s>.%n";
  private static final String EXPECTED_SINGLE = "%nExpecting value <%s> in field <%s> but was <%s> in <%s>.%n";
  private static final String COMPARISON = "Comparison was performed on all fields";
  private static final String EXCLUDING = " but <%s>";

  /**
   * Creates a new <code>{@link ShouldBeEqualToIgnoringFields}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param rejectedFields fields name not matching
   * @param rejectedValues rejected fields values
   * @param expectedValues expected fields values
   * @param ignoredFields fields which are not base the lenient equality
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualToIgnoringGivenFields(Object actual, List<String> rejectedFields,
                                                                       List<Object> rejectedValues,
                                                                       List<Object> expectedValues,
                                                                       List<String> ignoredFields) {
    if (rejectedFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldBeEqualToIgnoringFields(actual, rejectedFields.get(0), rejectedValues.get(0),
                                                 expectedValues.get(0));
      }
      return new ShouldBeEqualToIgnoringFields(actual, rejectedFields.get(0), rejectedValues.get(0),
                                               expectedValues.get(0), ignoredFields);
    }
    if (ignoredFields.isEmpty()) {
      return new ShouldBeEqualToIgnoringFields(actual, rejectedFields, rejectedValues, expectedValues);
    }
    return new ShouldBeEqualToIgnoringFields(actual, rejectedFields, rejectedValues, expectedValues, ignoredFields);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, List<String> rejectedFields, List<Object> rejectedValues,
                                        List<Object> expectedValues, List<String> ignoredFields) {
    super(EXPECTED_MULTIPLE + COMPARISON + EXCLUDING, expectedValues, rejectedFields, rejectedValues, actual,
          ignoredFields);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, String rejectedField, Object rejectedValue, Object expectedValue,
                                        List<String> ignoredFields) {
    super(EXPECTED_SINGLE + COMPARISON + EXCLUDING, expectedValue, rejectedField, rejectedValue, actual, ignoredFields);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, List<String> rejectedFields, List<Object> rejectedValues,
                                        List<Object> expectedValue) {
    super(EXPECTED_MULTIPLE + COMPARISON, expectedValue, rejectedFields, rejectedValues, actual);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, String rejectedField, Object rejectedValue,
                                        Object expectedValue) {
    super(EXPECTED_SINGLE + COMPARISON, expectedValue, rejectedField, rejectedValue, actual);
  }

}
