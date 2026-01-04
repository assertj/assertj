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
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Throwables.getStackTrace;
import static org.mockito.internal.util.collections.Sets.newSet;

import java.util.Set;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldNotContainCharSequence#shouldNotContain(Throwable, CharSequence[], Set)})}</code> and
 * <code>{@link ShouldNotContainCharSequence#shouldNotContain(Throwable, CharSequence)})}</code>.
 *
 * @author Usman Shaikh
 */
class ShouldNotContainThrowable_create_Test {
  @Test
  void should_create_error_message_with_escaping_percent() {
    // GIVEN
    RuntimeException actual = new RuntimeException("You know nothing % Jon Snow");
    // WHEN
    String errorMessage = shouldNotContain(actual, "You know nothing %").create(new TestDescription("TEST"));
    // THEN
    then(errorMessage).isEqualTo("[TEST] %n" +
                                 "Expecting throwable message:%n" +
                                 "  \"You know nothing %% Jon Snow\"%n" +
                                 "not to contain:%n" +
                                 "  \"You know nothing %%\"%n" +
                                 "but did:%n" +
                                 "%n" +
                                 "Throwable that failed the check:%n" +
                                 "%n%s", getStackTrace(actual));
  }

  @Test
  void should_create_error_message_with_several_values_not_found() {
    // GIVEN
    RuntimeException actual = new RuntimeException("You know nothing Jon Snow");
    String[] sequence = array("You", "know", "nothing");
    Set<String> found = newSet("You", "know", "nothing");
    // WHEN
    String errorMessage = shouldNotContain(actual, sequence, found).create(new TestDescription("TEST"));
    // THEN
    then(errorMessage).isEqualTo("[TEST] %n" +
                                 "Expecting throwable message:%n" +
                                 "  \"You know nothing Jon Snow\"%n" +
                                 "not to contain:%n" +
                                 "  [\"You\", \"know\", \"nothing\"]%n" +
                                 "but found:%n" +
                                 "  [\"You\", \"know\", \"nothing\"]%n" +
                                 "%n" +
                                 "Throwable that failed the check:%n" +
                                 "%n%s", getStackTrace(actual));
  }
}
