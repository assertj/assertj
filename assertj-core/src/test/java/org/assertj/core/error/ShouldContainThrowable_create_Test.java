/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.set;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.util.Set;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Benoit Dupont
 */
class ShouldContainThrowable_create_Test {
  @Test
  void should_create_error_message_with_escaping_percent() {
    // GIVEN
    RuntimeException actual = new RuntimeException("You know nothing %");
    // WHEN
    String errorMessage = shouldContain(actual, "You know nothing % Jon Snow").create(new TestDescription("TEST"));
    // THEN
    then(errorMessage).isEqualTo("[TEST] %n" +
                                 "Expecting throwable message:%n" +
                                 "  \"You know nothing %%\"%n" +
                                 "to contain:%n" +
                                 "  \"You know nothing %% Jon Snow\"%n" +
                                 "but did not.%n" +
                                 "%n" +
                                 "Throwable that failed the check:%n" +
                                 "%n%s", getStackTrace(actual));
  }

  @Test
  void should_create_error_message_with_several_values_not_found() {
    // GIVEN
    RuntimeException actual = new RuntimeException("You know nothing");
    String[] sequence = array("You", "know", "nothing", "Jon", "Snow");
    Set<String> notFound = set("Jon", "Snow");
    // WHEN
    String errorMessage = shouldContain(actual, sequence, notFound).create(new TestDescription("TEST"));
    // THEN
    then(errorMessage).isEqualTo("[TEST] %n" +
                                 "Expecting throwable message:%n" +
                                 "  \"You know nothing\"%n" +
                                 "to contain:%n" +
                                 "  [\"You\", \"know\", \"nothing\", \"Jon\", \"Snow\"]%n" +
                                 "but could not find:%n" +
                                 "  [\"Jon\", \"Snow\"]%n" +
                                 "%n" +
                                 "Throwable that failed the check:%n" +
                                 "%n%s", getStackTrace(actual));
  }
}
