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
package org.assertj.tests.core.api.recursive.assertion;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_OBJECT_AND_VALUE;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_OBJECT_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_VALUE_ONLY;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveAssertionDriver_OptionalPolicy_Test extends AbstractRecursiveAssertionDriverTestBase {

  @ParameterizedTest
  @MethodSource(value = "wrapper_object_with_Optional_and_primitive_Optional_types")
  void should_assert_over_optional_value_but_not_the_optional_object_when_policy_is_OPTIONAL_VALUE_ONLY(Wrapper<?> testObject) {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withOptionalAssertionPolicy(OPTIONAL_VALUE_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver driver = new RecursiveAssertionDriver(configuration);
    // WHEN
    List<FieldLocation> failedFields = driver.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(new FieldLocation("optional.value"));
  }

  @ParameterizedTest
  @MethodSource(value = "wrapper_object_with_Optional_and_primitive_Optional_types")
  void should_assert_over_optional_object_but_not_its_value_when_policy_is_OPTIONAL_OBJECT_ONLY(Wrapper<?> testObject) {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withOptionalAssertionPolicy(OPTIONAL_OBJECT_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver driver = new RecursiveAssertionDriver(configuration);
    // WHEN
    List<FieldLocation> failedFields = driver.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(new FieldLocation("optional"));
  }

  @ParameterizedTest
  @MethodSource(value = "wrapper_object_with_Optional_and_primitive_Optional_types")
  void should_assert_over_optional_object_and_its_value_when_policy_is_OPTIONAL_OBJECT_AND_VALUE(Wrapper<?> testObject) {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withOptionalAssertionPolicy(OPTIONAL_OBJECT_AND_VALUE)
                                                                                   .build();
    RecursiveAssertionDriver driver = new RecursiveAssertionDriver(configuration);
    // WHEN
    List<FieldLocation> failedFields = driver.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(new FieldLocation("optional"), new FieldLocation("optional.value"));
  }

  private static Stream<Wrapper> wrapper_object_with_Optional_and_primitive_Optional_types() {
    return Stream.of(new Wrapper(Optional.of("foo")), new Wrapper(OptionalLong.of(1l)),
                     new Wrapper(OptionalDouble.of(1.0)), new Wrapper(OptionalInt.of(1)));
  }

  static class Wrapper<T> {
    T optional;

    Wrapper(T optional) {
      this.optional = optional;
    }

    @Override
    public String toString() {
      return optional.getClass().getSimpleName();
    }
  }

}
