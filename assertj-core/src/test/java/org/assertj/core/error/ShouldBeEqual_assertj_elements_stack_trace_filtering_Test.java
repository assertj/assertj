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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.StackTraceUtils.hasStackTraceElementRelatedToAssertJ;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeEqual}</code> related to AssertionError stack trace filtering.
 *
 * @author Joel Costigliola
 */
class ShouldBeEqual_assertj_elements_stack_trace_filtering_Test {

  @Test
  void assertj_elements_should_be_removed_from_assertion_error_stack_trace() {
    // GIVEN
    Fail.setRemoveAssertJRelatedElementsFromStackTrace(true);
    // WHEN
    Throwable error = catchThrowable(() -> then("Xavi").isEqualTo("Xabi"));
    // THEN
    then(error).isInstanceOf(AssertionError.class);
    then(hasStackTraceElementRelatedToAssertJ(error)).isFalse();
  }

  @Test
  void assertj_elements_should_be_kept_in_assertion_error_stack_trace() {
    // GIVEN
    Fail.setRemoveAssertJRelatedElementsFromStackTrace(false);
    // WHEN
    Throwable error = catchThrowable(() -> then("Messi").isEqualTo("Ronaldo"));
    // THEN
    then(error).isInstanceOf(AssertionError.class);
    then(hasStackTraceElementRelatedToAssertJ(error)).isTrue();
  }

}
