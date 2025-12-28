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
package org.assertj.tests.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.Test;

class ListAssert_withElementAssert_Test {

  @Test
  void should_allow_chaining_element_specific_assertions_with_factory_returning_concrete_assertion() {
    // GIVEN
    List<String> actual = List.of("Homer", "Marge");
    AbstractListAssert<?, List<? extends String>, String, ObjectAssert<String>> underTest = assertThat(actual);
    AssertFactory<String, StringAssert> assertFactory = StringAssert::new;
    // WHEN
    AbstractListAssert<?, List<? extends String>, String, StringAssert> result = underTest.withElementAssert(assertFactory);
    // THEN
    result.hasSize(2)
          .first()
          .startsWith("Hom");
  }

  @Test
  void should_allow_chaining_element_specific_assertions_with_factory_returning_assertion_superclass() {
    // GIVEN
    List<String> actual = List.of("Homer", "Marge");
    AbstractListAssert<?, List<? extends String>, String, ObjectAssert<String>> underTest = assertThat(actual);
    AssertFactory<String, AbstractCharSequenceAssert<?, String>> assertFactory = StringAssert::new;
    // WHEN
    AbstractListAssert<?, List<? extends String>, String, AbstractCharSequenceAssert<?, String>> result = underTest.withElementAssert(assertFactory);
    // THEN
    result.hasSize(2)
          .first()
          .startsWith("Hom");
  }

}
