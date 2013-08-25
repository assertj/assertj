/*
 * Created on Mar 19, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2012 the original author or authors.
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

  /**
   * Creates a new </code>{@link ShouldBeEqualToIgnoringFields}</code>.
   * @param actual the actual value in the failed assertion.
   * @param rejectedFields fields name not matching
   * @param expectedValues fields value not matching
   * @param ignoredFields fields which are not base the lenient equality
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualToIgnoringGivenFields(Object actual, List<String> rejectedFields,
                                                                        List<Object> expectedValues, List<String> ignoredFields) {
    if (rejectedFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldBeEqualToIgnoringFields(actual, rejectedFields.get(0), expectedValues.get(0));
      } else {
        return new ShouldBeEqualToIgnoringFields(actual, rejectedFields.get(0), expectedValues.get(0), ignoredFields);
      }
    } else {
      if (ignoredFields.isEmpty()) {
        return new ShouldBeEqualToIgnoringFields(actual, rejectedFields, expectedValues);
      } else {
        return new ShouldBeEqualToIgnoringFields(actual, rejectedFields, expectedValues, ignoredFields);
      }
    }
  }

  private ShouldBeEqualToIgnoringFields(Object actual, List<String> rejectedFields, List<Object> expectedValues,
                                         List<String> ignoredFields) {
    super("\nExpecting values:\n  <%s>\nin fields:\n  <%s>\nof <%s>.\nComparison was performed on all fields but <%s>",
        expectedValues, rejectedFields, actual, ignoredFields);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, String rejectedField, Object expectedValue, List<String> ignoredFields) {
    super("\nExpecting value <%s> in field <%s> of <%s>.\nComparison was performed on all fields but <%s>", expectedValue,
        rejectedField, actual, ignoredFields);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, List<String> rejectedFields, List<Object> expectedValue) {
    super("\nExpecting values:\n  <%s>\nin fields:\n  <%s>\nof <%s>.\nComparison was performed on all fields", expectedValue,
        rejectedFields, actual);
  }

  private ShouldBeEqualToIgnoringFields(Object actual, String rejectedField, Object expectedValue) {
    super("\nExpecting value <%s> in field <%s> of <%s>.\nComparison was performed on all fields", expectedValue, rejectedField,
        actual);
  }

}
