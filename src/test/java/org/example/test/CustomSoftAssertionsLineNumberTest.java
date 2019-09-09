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
 * Copyright 2012-2019 the original author or authors.
 */
package org.example.test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The assertions classes have to be in a package other than org.assertj to test
 * the behavior of line numbers for assertions defined outside the assertj package
 */
class CustomSoftAssertionsLineNumberTest {

  // ignore because of failure only happening in Travis CI https://travis-ci.org/joel-costigliola/assertj-core/builds/351639100
  @Test
  @Disabled
  void should_print_line_numbers_of_failed_assertions_even_if_custom_assertion_in_non_assertj_package() throws Exception {
    // GIVEN
    MyProjectSoftAssertions softly = new MyProjectSoftAssertions();
    softly.assertThat(new MyProjectClass("v1")).hasValue("v2");
    // WHEN
    AssertionError error = catchThrowableOfType(softly::assertAll, AssertionError.class);
    // THEN
    assertThat(error).hasMessageContaining(format("Expecting value to be <v2> but was <v1>:%n" +
                                                  "at CustomSoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions_even_if_custom_assertion_in_non_assertj_package(CustomSoftAssertionsLineNumberTest.java:34)"));
  }

}
