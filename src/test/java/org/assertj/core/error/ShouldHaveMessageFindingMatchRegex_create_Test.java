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
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveMessageFindingMatchRegex.shouldHaveMessageFindingMatchRegex;
import static org.assertj.core.util.Throwables.getStackTrace;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * @author David Haccoun
 */
public class ShouldHaveMessageFindingMatchRegex_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    String regex = "regex";
    RuntimeException actual = new RuntimeException("error %s message");
    // WHEN
    String errorMessage = shouldHaveMessageFindingMatchRegex(actual, regex).create(new TestDescription("TEST"));
    // THEN
    assertThat(errorMessage).isEqualTo("[TEST] %n" +
                                       "Expecting message:%n" +
                                       "  <\"error %%s message\">%n" +
                                       "to be found for regex:%n" +
                                       "  <\"regex\">%n" +
                                       "but did not.%n" +
                                       "%n" +
                                       "Throwable that failed the check:%n" +
                                       "%n%s",
                                       getStackTrace(actual));
  }

}
