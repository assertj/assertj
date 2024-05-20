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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions from method")
class EntryPointAssertions_from_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("fromFunctions")
  void should_return_same_function(Function<Function<Object, Object>, Function<Object, Object>> fromFunction) {
    // GIVEN
    Function<Object, Object> identity = t -> t;
    // WHEN
    Function<Object, Object> fromResult = fromFunction.apply(identity);
    // THEN
    then(fromResult).isSameAs(identity);
  }

  private static <F, T> Stream<Function<Function<F, T>, Function<F, T>>> fromFunctions() {
    return Stream.of(Assertions::from, BDDAssertions::from, withAssertions::from);
  }

}
