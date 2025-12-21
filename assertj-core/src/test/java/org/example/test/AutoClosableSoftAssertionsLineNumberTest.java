/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;

/**
 * The assertions classes have to be in a package other than org.assertj to test
 * the behavior of line numbers for assertions defined outside the assertj package
 */
class AutoClosableSoftAssertionsLineNumberTest {

  @Test
  void should_print_line_numbers_of_failed_assertions() {
    // noinspection resource
    AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions();
    softly.assertThat(1)
          .isLessThan(0)
          .isLessThan(1);
    // WHEN
    var error = expectAssertionError(softly::close);
    // THEN
    //@format:off
    then(error).hasMessageContainingAll("""
                                        Expecting actual:
                                          1
                                        to be less than:
                                          0\s
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions(AutoClosableSoftAssertionsLineNumberTest.java:35)",
                                        """
                                        Expecting actual:
                                          1
                                        to be less than:
                                          1\s
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions(AutoClosableSoftAssertionsLineNumberTest.java:36)");
    //@format:on
  }
}
