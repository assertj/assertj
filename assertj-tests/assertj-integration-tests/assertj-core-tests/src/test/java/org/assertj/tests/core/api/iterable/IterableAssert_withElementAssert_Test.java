/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashSet;
import java.util.List;

import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IterableAssert_withElementAssert_Test {

  @Nested
  class With_List {

    @Test
    void should_allow_chaining_element_specific_assertions_with_factory_returning_concrete_assertion() {
      // GIVEN
      Iterable<String> actual = List.of("Homer", "Marge");
      AssertFactory<String, StringAssert> assertFactory = StringAssert::new;
      AbstractIterableAssert<?, Iterable<? extends String>, String, ObjectAssert<String>> underTest = assertThat(actual);
      // WHEN
      AbstractIterableAssert<?, Iterable<? extends String>, String, StringAssert> result = underTest.withElementAssert(assertFactory);
      // THEN
      result.hasSize(2)
            .first()
            .startsWith("Hom");
    }

    @Test
    void should_allow_chaining_element_specific_assertions_with_factory_returning_assertion_superclass() {
      // GIVEN
      Iterable<String> actual = List.of("Homer", "Marge");
      AbstractIterableAssert<?, Iterable<? extends String>, String, ObjectAssert<String>> underTest = assertThat(actual);
      AssertFactory<String, AbstractCharSequenceAssert<?, String>> assertFactory = StringAssert::new;
      // WHEN
      AbstractIterableAssert<?, Iterable<? extends String>, String, AbstractCharSequenceAssert<?, String>> result = underTest.withElementAssert(assertFactory);
      // THEN
      result.hasSize(2)
            .first()
            .startsWith("Hom");
    }

  }

  @Nested
  class With_Set {

    @Test
    void should_allow_chaining_element_specific_assertions_with_factory_returning_concrete_assertion() {
      // GIVEN
      Iterable<String> actual = new LinkedHashSet<>(List.of("Homer", "Marge"));
      AssertFactory<String, StringAssert> assertFactory = StringAssert::new;
      AbstractIterableAssert<?, Iterable<? extends String>, String, ObjectAssert<String>> underTest = assertThat(actual);
      // WHEN
      AbstractIterableAssert<?, Iterable<? extends String>, String, StringAssert> result = underTest.withElementAssert(assertFactory);
      // THEN
      result.hasSize(2)
            .first()
            .startsWith("Hom");
    }

    @Test
    void should_allow_chaining_element_specific_assertions_with_factory_returning_assertion_superclass() {
      // GIVEN
      Iterable<String> actual = new LinkedHashSet<>(List.of("Homer", "Marge"));
      AbstractIterableAssert<?, Iterable<? extends String>, String, ObjectAssert<String>> underTest = assertThat(actual);
      AssertFactory<String, AbstractCharSequenceAssert<?, String>> assertFactory = StringAssert::new;
      // WHEN
      AbstractIterableAssert<?, Iterable<? extends String>, String, AbstractCharSequenceAssert<?, String>> result = underTest.withElementAssert(assertFactory);
      // THEN
      result.hasSize(2)
            .first()
            .startsWith("Hom");
    }

  }

}
