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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveMessageMatchingRegex.shouldHaveMessageMatchingRegex;

import org.assertj.core.internal.TestDescription;
import org.junit.Test;

public class ShouldHaveMessageMatchingRegex_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    String regex = "regex";
    RuntimeException actual = new RuntimeException("error message");
    // WHEN
    String errorMessage = shouldHaveMessageMatchingRegex(actual, regex).create(new TestDescription("TEST"));
    // THEN
    assertThat(errorMessage).isEqualTo(format("[TEST] %n" +
                                              "Expecting message:%n" +
                                              "  <\"error message\">%n" +
                                              "to match regex:%n" +
                                              "  <\"regex\">%n" +
                                              "but did not."));
  }

}
