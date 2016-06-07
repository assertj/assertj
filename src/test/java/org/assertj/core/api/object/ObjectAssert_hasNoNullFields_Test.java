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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.object;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectAssert#hasNoNullFieldsExcept(String)}</code>.
 *
 * @author Johannes Brodwall
 */
public class ObjectAssert_hasNoNullFields_Test extends ObjectAssertBaseTest {

  public static final String FIELD_NAME = "color"; // field in org.assertj.core.test.Person

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasFieldOrProperty(FIELD_NAME);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasFieldOrProperty(getInfo(assertions), getActual(assertions), FIELD_NAME);
  }

  @Test
  public void should_fail_if_required_field_is_null() {
    Jedi jedi = new Jedi("Yoda", null);

    try {
      assertThat(jedi).hasNoNullFieldsExcept("name", "strangeNotReadablePrivateField");
    } catch (AssertionError e) {
      assertThat(e).hasMessage(
              format("%nExpecting%n  <Yoda the Jedi>%nto have no property or a field with value null, but <\"lightSaberColor\"> was null.%nIgnored fields: <[\"name\", \"strangeNotReadablePrivateField\"]>"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_some_field_is_null() {
    Jedi jedi = new Jedi("Yoda", "Blue");

    try {
      assertThat(jedi).hasNoNullFields();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(
              format("%nExpecting%n  <Yoda the Jedi>%nto have no property or a field with value null, but <\"strangeNotReadablePrivateField\"> was null.%nComparison was performed on all fields"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_all_fields_are_set() {
    Jedi jedi = new Jedi("Yoda", null);
    assertThat(jedi).hasNoNullFieldsExcept("lightSaberColor", "strangeNotReadablePrivateField");
  }
}
