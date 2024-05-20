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

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.ITERABLE;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.condition.AllOf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions allOf method")
class EntryPointAssertions_allOf_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("allOfWithArrayFactories")
  <T> void should_create_allOf_condition_from_condition_array(Function<Condition<T>[], Condition<T>> allOfFactory) {
    // GIVEN
    Condition<T> condition1 = new TestCondition<>("condition 1");
    Condition<T> condition2 = new TestCondition<>("condition 2");
    // WHEN
    Condition<T> allOfCondition = allOfFactory.apply(array(condition1, condition2));
    // THEN
    then(allOfCondition).isInstanceOf(AllOf.class)
                        .extracting("conditions", as(ITERABLE))
                        .containsExactly(condition1, condition2);
  }

  @SuppressWarnings("unchecked")
  private static <T> Stream<Function<Condition<T>[], Condition<T>>> allOfWithArrayFactories() {
    return Stream.of(Assertions::allOf, BDDAssertions::allOf, withAssertions::allOf);
  }

  @ParameterizedTest
  @MethodSource("allOfWithCollectionFactories")
  <T> void should_create_allOf_condition_from_condition_collection(Function<Collection<Condition<T>>, Condition<T>> allOfFactory) {
    // GIVEN
    Condition<T> condition1 = new TestCondition<>("condition 1");
    Condition<T> condition2 = new TestCondition<>("condition 2");
    // WHEN
    Condition<T> allOfCondition = allOfFactory.apply(list(condition1, condition2));
    // THEN
    then(allOfCondition).isInstanceOf(AllOf.class)
                        .extracting("conditions", as(ITERABLE))
                        .containsExactly(condition1, condition2);
  }

  private static <T> Stream<Function<Collection<Condition<T>>, Condition<T>>> allOfWithCollectionFactories() {
    return Stream.of(Assertions::allOf, BDDAssertions::allOf, withAssertions::allOf);
  }

}
