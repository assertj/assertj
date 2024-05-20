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
import org.assertj.core.api.filter.InFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions in filter method")
class EntryPointAssertions_in_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("inFunctions")
  void should_create_allOf_condition_from_condition_array(Function<Object[], InFilter> inFunction) {
    // GIVEN
    String[] names = { "joe", "jack" };
    // WHEN
    InFilter inFilter = inFunction.apply(names);
    // THEN
    then(inFilter).extracting("filterParameter")
                  .isEqualTo(names);
  }

  private static Stream<Function<Object[], InFilter>> inFunctions() {
    return Stream.of(Assertions::in, BDDAssertions::in, withAssertions::in);
  }

}
