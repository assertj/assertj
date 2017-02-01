/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectAssert#hasFieldOrPropertyWithValue(String, Object)}</code>.
 * 
 * @author Libor Ondrusek
 */
public class ObjectAssert_hasFieldOrPropertyWithValue_Test extends ObjectAssertBaseTest {

  public static final String FIELD_NAME = "name"; // field in org.assertj.core.test.Person
  public static final String FIELD_VALUE = "Yoda"; // field value in org.assertj.core.test.Person

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasFieldOrPropertyWithValue(FIELD_NAME, FIELD_VALUE);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasFieldOrPropertyWithValue(getInfo(assertions), getActual(assertions), FIELD_NAME,
                                                      FIELD_VALUE);
  }

  @Test
  public void should_pass_if_both_are_null() {
    Jedi jedi = new Jedi(null, "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue(FIELD_NAME, null);
  }

  @Test
  public void should_fail_if_given_field_or_property_does_not_exist() {
    thrown.expectAssertionError("%nExpecting%n  <Yoda the Jedi>%nto have a property or a field named <\"not_exists_in_jedi_object\">");

    Jedi jedi = new Jedi("Yoda", "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue("not_exists_in_jedi_object", FIELD_VALUE);
  }

  @Test
  public void should_fail_if_field_or_property_value_is_not_equal_to_the_expected_value() {
    thrown.expectAssertionError("%nExpecting%n  <Yoda the Jedi>%nto have a property or a field named <\"name\"> with value%n  <1000>%nbut value was:%n  <\"Yoda\">");

    Jedi jedi = new Jedi("Yoda", "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue(FIELD_NAME, 1000);
  }

  @Test
  public void should_fail_if_field_or_property_value_is_not_null_when_expected_value_is() {
    thrown.expectAssertionError("%nExpecting%n  <Yoda the Jedi>%nto have a property or a field named <\"name\"> with value%n  <null>%nbut value was:%n  <\"Yoda\">");

    Jedi jedi = new Jedi("Yoda", "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue(FIELD_NAME, null);
  }

  @Test
  public void should_fail_when_property_or_field_value_is_null_and_expected_value_is_not() {
    thrown.expectAssertionError("%nExpecting%n  <null the Jedi>%nto have a property or a field named <\"name\"> with value%n  <\"Yoda\">%nbut value was:%n  <null>");

    Jedi jedi = new Jedi(null, "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue(FIELD_NAME, FIELD_VALUE);
  }

  @Test
  public void should_fail_if_given_field_or_property_name_is_null() {
    thrown.expectIllegalArgumentException("The name of the property/field to read should not be null");

    Jedi jedi = new Jedi("Yoda", "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue(null, FIELD_VALUE);
  }
}
