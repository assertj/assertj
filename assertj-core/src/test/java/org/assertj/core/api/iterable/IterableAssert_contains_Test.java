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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#contains(Object...)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class IterableAssert_contains_Test extends IterableAssertBaseTest {

  private Object[] values = { "Yoda", "Luke" };

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.contains(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContains(getInfo(assertions), getActual(assertions), values);
  }

  @Test
  void should_disambiguate_enum_types_in_failure_message() {
    // GIVEN
    List<Object> actual = List.of(org.assertj.core.internal.objects.pkg1.Example.VALUE);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
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
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(error).hasMessageContaining("overridden (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("overridden (org.assertj.core.internal.objects.pkg2.Example@");
  }

  @Test
  void should_disambiguate_enum_elements_when_collections_have_different_representations() {
    // GIVEN
    List<Object> actual = Arrays.asList(org.assertj.core.internal.objects.pkg1.Example.VALUE, "other", null);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(error).hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg2.Example@")
               .hasMessageContaining("\"other\", null");
  }

  @Test
  void should_not_disambiguate_repeated_values_of_the_same_enum_type() {
    // GIVEN
    List<Object> actual = Arrays.asList(org.assertj.core.internal.objects.pkg1.Example.VALUE,
                                        org.assertj.core.internal.objects.pkg1.Example.VALUE, null);
    var expected = org.assertj.core.internal.objects.pkg1.Example.OTHER;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(error).hasMessage("%nExpecting ArrayList:%n  [VALUE, VALUE, null]%nto contain:%n  [OTHER]%nbut could not find the following element(s):%n  [OTHER]%n".formatted());
  }

  @Test
  void should_preserve_custom_representation_of_collections() {
    // GIVEN
    List<Object> actual = List.of(org.assertj.core.internal.objects.pkg1.Example.VALUE);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    Representation representation = value -> "custom group";
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).withRepresentation(representation).contains(expected));
    // THEN
    then(error).hasMessage("%nExpecting List12:%n  custom group%nto contain:%n  custom group%nbut could not find the following element(s):%n  custom group%n".formatted());
  }

  @Test
  void should_preserve_custom_standard_representation() {
    // GIVEN
    List<Object> actual = List.of(org.assertj.core.internal.objects.pkg1.Example.VALUE);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    var representation = new StandardRepresentation() {
      @Override
      protected String fallbackToStringOf(Object value) {
        return value instanceof Enum<?> ? "custom enum" : super.fallbackToStringOf(value);
      }
    };
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).withRepresentation(representation).contains(expected));
    // THEN
    then(error).hasMessage("%nExpecting List12:%n  [custom enum]%nto contain:%n  [custom enum]%nbut could not find the following element(s):%n  [custom enum]%n".formatted());
  }

  @Test
  void should_preserve_registered_enum_formatters() {
    // GIVEN
    List<Object> actual = List.of(org.assertj.core.internal.objects.pkg1.Example.VALUE);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    StandardRepresentation.registerFormatterForType(org.assertj.core.internal.objects.pkg1.Example.class, value -> "custom enum");
    StandardRepresentation.registerFormatterForType(org.assertj.core.internal.objects.pkg2.Example.class, value -> "custom enum");
    try {
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).contains(expected));
      // THEN
      then(error).hasMessage("%nExpecting List12:%n  [custom enum]%nto contain:%n  [custom enum]%nbut could not find the following element(s):%n  [custom enum]%n".formatted());
    } finally {
      StandardRepresentation.removeAllRegisteredFormatters();
    }
  }

  @Test
  void should_disambiguate_enum_elements_of_a_single_pass_iterable() {
    // GIVEN
    var iterator = List.<Object> of(org.assertj.core.internal.objects.pkg1.Example.VALUE).iterator();
    Iterable<Object> actual = () -> iterator;
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(error).hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg2.Example@");
  }

  @Test
  void should_disambiguate_enum_elements_of_a_self_referencing_collection() {
    // GIVEN
    var actual = new ArrayList<Object>();
    actual.add(actual);
    actual.add(org.assertj.core.internal.objects.pkg1.Example.VALUE);
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(error).hasMessageContaining("(this instance)")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg2.Example@");
  }
}
