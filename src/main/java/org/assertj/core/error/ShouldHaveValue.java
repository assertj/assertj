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

public class ShouldHaveValue extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_VALUE_BUT_DID_NOT = "%nExpecting <%s> to have value:%n  <%s>%nbut did not.";
  private static final String SHOULD_HAVE_VALUE = "%nExpecting <%s> to have value:%n  <%s>%nbut had:%n  <%s>%nto update target object:%n  <%s>";

  private <REF> ShouldHaveValue(Object actual, REF actualValue, REF expectedValue, Object objectToUpdate) {
    super(SHOULD_HAVE_VALUE, actual, expectedValue, actualValue, objectToUpdate);
  }

  private <REF> ShouldHaveValue(Object actual, REF expectedValue) {
    super(SHOULD_HAVE_VALUE_BUT_DID_NOT, actual, expectedValue);
  }

  public static <REF> ErrorMessageFactory shouldHaveValue(Object actual, REF actualValue, REF expectedValue,
                                                          Object objectToUpdate) {
    return new ShouldHaveValue(actual, actualValue, expectedValue, objectToUpdate);
  }

  public static <REF> ErrorMessageFactory shouldHaveValue(Object actual, REF expectedValue) {
    return new ShouldHaveValue(actual, expectedValue);
  }
}
