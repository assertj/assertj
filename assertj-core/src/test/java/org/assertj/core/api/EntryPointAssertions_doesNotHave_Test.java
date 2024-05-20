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

import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.condition.DoesNotHave;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions doesNotHave method")
class EntryPointAssertions_doesNotHave_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("doesNotHaveFactories")
  <T> void should_create_allOf_condition_from_condition_array(Function<Condition<T>, DoesNotHave<T>> doesNotHaveFactory) {
    // GIVEN
    Condition<T> condition = new TestCondition<>("condition");
    // WHEN
    DoesNotHave<T> doesNotHave = doesNotHaveFactory.apply(condition);
    // THEN
    then(doesNotHave).extracting("condition")
                     .isEqualTo(condition);

  }

  private static <T> Stream<Function<Condition<T>, DoesNotHave<T>>> doesNotHaveFactories() {
    return Stream.of(Assertions::doesNotHave, BDDAssertions::doesNotHave, withAssertions::doesNotHave);
  }

}
