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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SoftAssertions}, to verify that proxied custom assertions inheriting from {@link AbstractListAssert} do not throw {@link ClassCastException}.
 *
 * @author HuitaePark
 */
public class SoftAssertions_proxied_AbstractListAssert_Test {

  @Test
  void should_not_throw_class_cast_exception_when_calling_methods_with_array_args_on_proxied_custom_assertion() {
    // GIVEN
    List<String> actual = list("a", "b");
    List<String> expected = list("a", "b");

    // WHEN / THEN
    thenNoException().isThrownBy(() -> {
      assertSoftly(softly -> softly.proxy(TestListAssert.class, List.class, actual)
                                   .containsExactlyInAnyOrderElementsOf(expected));
    });
  }

  static class TestListAssert extends AbstractListAssert<TestListAssert, List<String>, String, ObjectAssert<String>> {

    public TestListAssert(List<String> actual) {
      super(actual, TestListAssert.class);
    }

    @Override
    protected TestListAssert newAbstractIterableAssert(Iterable<? extends String> iterable) {
      return new TestListAssert(newArrayList(iterable));
    }

    @Override
    protected ObjectAssert<String> toAssert(String value, String description) {
      return new ObjectAssert<>(value);
    }

    @Override
    protected ObjectAssert<String> toAssert(String value) {
      return new ObjectAssert<>(value);
    }
  }
}
