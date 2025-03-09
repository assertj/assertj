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
 * Copyright 2012-2025 the original author or authors.
 */
package org.example.test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.Throwables.removeAssertJRelatedElementsFromStackTrace;
import static org.assertj.tests.core.testkit.StackTraceUtils.checkNoAssertjStackTraceElementIn;
import static org.assertj.tests.core.testkit.StackTraceUtils.hasAssertJStackTraceElement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 */
class Throwables_removeAssertJElementFromStackTrace_Test {

  @Test
  void should_add_stack_trace_of_current_thread() {
    // GIVEN
    Throwable throwable = catchThrowable(this::throwAssertJThrowable);
    Assertions.assertThat(hasAssertJStackTraceElement(throwable)).isTrue();
    // WHEN
    removeAssertJRelatedElementsFromStackTrace(throwable);
    // THEN
    checkNoAssertjStackTraceElementIn(throwable);
  }

  private void throwAssertJThrowable() throws AssertJThrowable {
    throw new AssertJThrowable();
  }

  private static class AssertJThrowable extends Throwable {
  }

}
