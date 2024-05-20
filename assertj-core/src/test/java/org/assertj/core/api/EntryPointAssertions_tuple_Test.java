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
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions tuple method")
class EntryPointAssertions_tuple_Test extends EntryPointAssertionsBaseTest {

  private static final int AGE = 800;
  private static final String JEDI = "Jedi";
  private static final String YODA = "Yoda";

  @ParameterizedTest
  @MethodSource("tupleFactories")
  void should_create_tuple(Function<Object[], Tuple> tupleFactory) {
    // GIVEN
    Object[] values = { YODA, AGE, JEDI };
    // WHEN
    Tuple result = tupleFactory.apply(values);
    // THEN
    then(result).isEqualTo(new Tuple(YODA, AGE, JEDI));
  }

  private static Stream<Function<Object[], Tuple>> tupleFactories() {
    return Stream.of(Assertions::tuple, BDDAssertions::tuple, withAssertions::tuple);
  }

}
