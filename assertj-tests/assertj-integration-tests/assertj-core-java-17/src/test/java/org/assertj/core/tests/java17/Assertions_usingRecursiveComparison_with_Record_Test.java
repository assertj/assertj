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
package org.assertj.core.tests.java17;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.tests.java17.util.AssertionErrors.expectAssertionError;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Emanuel Trandafir
 */
class Assertions_usingRecursiveComparison_with_Record_Test {

  @AfterAll
  static void afterAll() {
    Assertions.setExtractBareNamePropertyMethods(true);
  }

  @Nested
  class with_flat_record {
    record User(Long id, String name) {
    }

    @ParameterizedTest(name = "and ExtractBareNamePropertyMethods is {0}")
    @ValueSource(booleans = { true, false })
    void recursiveComparison_should_pass_object_is_same(boolean extractBareName) {
      // GIVEN
      Assertions.setExtractBareNamePropertyMethods(extractBareName);

      // WHEN/THEN
      assertObjectsAreTheSame(new User(1L, "John Doe"), new User(1L, "John Doe"));
    }

    @ParameterizedTest(name = "and ExtractBareNamePropertyMethods is {0}")
    @ValueSource(booleans = { true, false })
    void recursiveComparison_should_fail_object_is_different(boolean extractBareName) {
      // GIVEN
      Assertions.setExtractBareNamePropertyMethods(extractBareName);

      // WHEN/THEN
      assertObjectsAreDifferent(new User(1L, "John Doe"), new User(99L, "OTHER User"));
    }

  }

  @Nested
  class with_nested_record {
    record User(Long id, Name name) {
    }

    record Name(String first, String last) {
    }

    @ParameterizedTest(name = "and ExtractBareNamePropertyMethods is {0}")
    @ValueSource(booleans = { true, false })
    void recursiveComparison_should_pass_object_is_same(boolean extractBareName) {
      // GIVEN
      Assertions.setExtractBareNamePropertyMethods(extractBareName);

      // WHEN/THEN
      assertObjectsAreTheSame(new User(1L, new Name("John", "Doe")), new User(1L, new Name("John", "Doe")));
    }

    @ParameterizedTest(name = "and ExtractBareNamePropertyMethods is {0}")
    @ValueSource(booleans = { true, false })
    void recursiveComparison_should_fail_object_is_different(boolean extractBareName) {
      // GIVEN
      Assertions.setExtractBareNamePropertyMethods(extractBareName);

      // WHEN/THEN
      assertObjectsAreDifferent(new User(1L, new Name("John", "Doe")),
                                new User(1L, new Name("John", "OTHER")));
    }

  }

  @Nested
  class with_double_nested_record {
    record User(Long id, Name name) {
    }

    record Name(String first, String last, List<Honorific> honorifics) {
    }

    record Honorific(String title) {
    }

    @ParameterizedTest(name = "and ExtractBareNamePropertyMethods is {0}")
    @ValueSource(booleans = { true, false })
    void recursiveComparison_should_pass_object_is_same(boolean extractBareName) {
      // GIVEN
      Assertions.setExtractBareNamePropertyMethods(extractBareName);

      // WHEN/THEN
      assertObjectsAreTheSame(new User(1L, new Name("John", "Doe", List.of(new Honorific("Dr.")))),
                              new User(1L, new Name("John", "Doe", List.of(new Honorific("Dr.")))));
    }

    @ParameterizedTest(name = "and ExtractBareNamePropertyMethods is {0}")
    @ValueSource(booleans = { true, false })
    void recursiveComparison_should_fail_object_is_different(boolean extractBareName) {
      // GIVEN
      Assertions.setExtractBareNamePropertyMethods(extractBareName);

      // WHEN/THEN
      assertObjectsAreDifferent(new User(1L, new Name("John", "Doe", List.of(new Honorific("PhD")))),
                                new User(1L, new Name("John", "Doe", List.of(new Honorific("OTHER")))));
    }

  }

  private static <T> void assertObjectsAreTheSame(T first, T second) {
    assertThat(first).usingRecursiveComparison().isEqualTo(second);
  }

  private static <T> void assertObjectsAreDifferent(T first, T second) {
    AssertionError assertionError = expectAssertionError(() -> assertThat(first).usingRecursiveComparison()
                                                                                .isEqualTo(second));

    then(assertionError).hasMessageContaining("Expecting actual:")
                        .hasMessageContaining("to be equal to:")
                        .hasMessageContaining("when recursively comparing field by field");
  }

}
