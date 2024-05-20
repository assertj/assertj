/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions setRemoveAssertJRelatedElementsFromStackTrace method")
class EntryPointAssertions_setRemoveAssertJRelatedElementsFromStackTrace_Test extends EntryPointAssertionsBaseTest {

  private static final Failures FAILURES = Failures.instance();
  private static final boolean DEFAULT_REMOVE_ASSERTJ_FROM_STACK_TRACE = FAILURES.isRemoveAssertJRelatedElementsFromStackTrace();

  @AfterEach
  void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    FAILURES.setRemoveAssertJRelatedElementsFromStackTrace(DEFAULT_REMOVE_ASSERTJ_FROM_STACK_TRACE);
  }

  @ParameterizedTest
  @MethodSource("setRemoveAssertJRelatedElementsFromStackTraceMethodsFunctions")
  void should_set_removeAssertJRelatedElementsFromStackTrace_value(Consumer<Boolean> setRemoveAssertJRelatedElementsFromStackTraceMethodsFunction) {
    // GIVEN
    boolean removeAssertJRelatedElementsFromStackTrace = !DEFAULT_REMOVE_ASSERTJ_FROM_STACK_TRACE;
    // WHEN
    setRemoveAssertJRelatedElementsFromStackTraceMethodsFunction.accept(removeAssertJRelatedElementsFromStackTrace);
    // THEN
    then(FAILURES.isRemoveAssertJRelatedElementsFromStackTrace()).isEqualTo(removeAssertJRelatedElementsFromStackTrace);
  }

  private static Stream<Consumer<Boolean>> setRemoveAssertJRelatedElementsFromStackTraceMethodsFunctions() {
    return Stream.of(Assertions::setRemoveAssertJRelatedElementsFromStackTrace,
                     BDDAssertions::setRemoveAssertJRelatedElementsFromStackTrace,
                     withAssertions::setRemoveAssertJRelatedElementsFromStackTrace);
  }

}
