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
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are
 * lenient equal by accepting fields failed.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ShouldBeEqualByComparingOnlyGivenFields extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEqualByComparingOnlyGivenFields}</code>.
   *
   *
   * @param actual the actual value in the failed assertion.
   * @param rejectedFields fields names not matching
   * @param rejectedValues fields values not matching
   * @param expectedValues expected fields values
   * @param acceptedFields fields on which is based the lenient equality
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualComparingOnlyGivenFields(Object actual, List<String> rejectedFields,
                                                                          List<Object> rejectedValues, List<Object> expectedValues,
                                                                          List<String> acceptedFields) {
    if (rejectedFields.size() == 1) {
      return new ShouldBeEqualByComparingOnlyGivenFields(actual, rejectedFields.get(0), rejectedValues.get(0), expectedValues.get(0),
          acceptedFields);
    }
    return new ShouldBeEqualByComparingOnlyGivenFields(actual, rejectedFields, rejectedValues, expectedValues,
                                                       acceptedFields);
  }

  private ShouldBeEqualByComparingOnlyGivenFields(Object actual, List<String> rejectedFields, List<Object> rejectedValues,
                                                  List<Object> expectedValue, List<String> acceptedFields) {
    super("%nExpecting values:%n  <%s>%nin fields:%n  <%s>%nbut were:%n  <%s>%nin <%s>.%nComparison was performed on fields:%n  <%s>",
        expectedValue, rejectedFields, rejectedValues, actual, acceptedFields);
  }

  private ShouldBeEqualByComparingOnlyGivenFields(Object actual, String rejectedField, Object rejectedValue, Object expectedValue,
                                                   List<String> acceptedFields) {
    super("%nExpecting value <%s> in field <%s> but was <%s> in <%s>", expectedValue, rejectedField, rejectedValue, actual,
        acceptedFields);
  }

}
