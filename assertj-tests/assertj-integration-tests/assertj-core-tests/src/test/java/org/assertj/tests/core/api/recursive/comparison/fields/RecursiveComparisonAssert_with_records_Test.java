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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Emanuel Trandafir
 */
class RecursiveComparisonAssert_with_records_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @Nested
  class with_flat_record {
    record User(Long id, String name) {
    }

    record UserClone(Long id, String name) {
    }

    @Test
    void recursiveComparison_should_pass_when_compared_records_have_the_same_data() {
      assertObjectsAreTheSame(new User(1L, "John Doe"), new User(1L, "John Doe"));
      assertObjectsAreTheSame(new User(1L, "John Doe"), new UserClone(1L, "John Doe"));
    }

    @Test
    void recursiveComparison_should_fail_when_compared_records_are_different() {
      assertObjectsAreDifferent(new User(1L, "John Doe"), new User(99L, "OTHER User"));
      assertObjectsAreDifferent(new User(1L, "John Doe"), new UserClone(99L, "OTHER User"));
    }

  }

  @Nested
  class with_nested_record {
    record User(Long id, Name name) {
    }

    record Name(String first, String last) {
    }

    record UserClone(Long id, NameClone name) {
    }

    record NameClone(String first, String last) {
    }

    @Test
    void recursiveComparison_should_pass_when_compared_records_have_the_same_data() {
      assertObjectsAreTheSame(new User(1L, new Name("John", "Doe")),
                              new User(1L, new Name("John", "Doe")));
      assertObjectsAreTheSame(new User(1L, new Name("John", "Doe")),
                              new UserClone(1L, new NameClone("John", "Doe")));
    }

    @Test
    void recursiveComparison_should_fail_when_compared_records_are_different() {
      assertObjectsAreDifferent(new User(1L, new Name("John", "Doe")),
                                new User(1L, new Name("John", "OTHER")));
      assertObjectsAreDifferent(new User(1L, new Name("John", "Doe")),
                                new UserClone(1L, new NameClone("John", "OTHER")));
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

    record UserClone(Long id, NameClone name) {
    }

    record NameClone(String first, String last, List<HonorificClone> honorifics) {
    }

    record HonorificClone(String title) {
    }

    @Test
    void recursiveComparison_should_pass_when_compared_records_have_the_same_data() {
      assertObjectsAreTheSame(new User(1L, new Name("John", "Doe", List.of(new Honorific("Dr.")))),
                              new User(1L, new Name("John", "Doe", List.of(new Honorific("Dr.")))));
      assertObjectsAreTheSame(new User(1L, new Name("John", "Doe", List.of(new Honorific("Dr.")))),
                              new UserClone(1L, new NameClone("John", "Doe", List.of(new HonorificClone("Dr.")))));
    }

    @Test
    void recursiveComparison_should_fail_when_compared_records_are_different() {
      assertObjectsAreDifferent(new User(1L, new Name("John", "Doe", List.of(new Honorific("PhD")))),
                                new User(1L, new Name("John", "Doe", List.of(new Honorific("OTHER")))));
      assertObjectsAreDifferent(new User(1L, new Name("John", "Doe", List.of(new Honorific("PhD")))),
                                new UserClone(1L, new NameClone("John", "Doe", List.of(new HonorificClone("OTHER")))));
    }

  }

  private <T> void assertObjectsAreTheSame(T first, T second) {
    assertThat(first).usingRecursiveComparison(recursiveComparisonConfiguration)
                     .isEqualTo(second);
  }

  private <T> void assertObjectsAreDifferent(T first, T second) {
    var assertionError = expectAssertionError(() -> assertThat(first).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                     .isEqualTo(second));

    then(assertionError).hasMessageContaining("Expecting actual:")
                        .hasMessageContaining("to be equal to:")
                        .hasMessageContaining("when recursively comparing field by field");
  }

}
