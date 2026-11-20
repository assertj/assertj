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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#containsExactly(Object...)}</code>.
 * 
 * @author Nicolas François
 */
class IterableAssert_containsExactly_Test extends IterableAssertBaseTest {

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.containsExactly("Yoda", "Luke");
  }

  @Override
  protected void verify_internal_effects() {
    Object[] values = { "Yoda", "Luke" };
    verify(iterables).assertContainsExactly(getInfo(assertions), getActual(assertions), values);
  }

  @Test
  void should_disambiguate_enum_types_in_failure_message() {
    // GIVEN
    List<Object> actual = List.of(org.assertj.core.internal.objects.pkg1.Example.VALUE);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).containsExactly(expected));
    // THEN
    then(error).hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg2.Example@");
  }

  @Test
  void should_disambiguate_enum_constant_with_overridden_toString_using_declaring_type() {
    // GIVEN
    List<Object> actual = List.of(org.assertj.core.internal.objects.pkg1.Example.OVERRIDDEN);
    var expected = org.assertj.core.internal.objects.pkg2.Example.OVERRIDDEN;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).containsExactly(expected));
    // THEN
    then(error).hasMessageContaining("overridden (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("overridden (org.assertj.core.internal.objects.pkg2.Example@");
  }

  @Test
  void should_disambiguate_enum_elements_at_different_indexes() {
    // GIVEN
    var first = org.assertj.core.internal.objects.pkg1.Example.VALUE;
    var second = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    List<Object> actual = List.of(first, second);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).containsExactly(second, first));
    // THEN
    then(error).hasMessageContaining("element at index 0: expected \"VALUE (org.assertj.core.internal.objects.pkg2.Example@")
               .hasMessageContaining("element at index 1: expected \"VALUE (org.assertj.core.internal.objects.pkg1.Example@");
  }
}
