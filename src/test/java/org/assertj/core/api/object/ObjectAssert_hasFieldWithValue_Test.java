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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectAssert#hasField(String, Object)}</code>.
 * 
 * @author Libor Ondrusek
 */
public class ObjectAssert_hasFieldWithValue_Test extends ObjectAssertBaseTest {

  public static final String FIELD_NAME = "name"; // field in org.assertj.core.test.Person
  public static final String FIELD_VALUE = "Yoda"; // field value in org.assertj.core.test.Person

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasField(FIELD_NAME, FIELD_VALUE);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasFieldWithValue(getInfo(assertions), getActual(assertions), FIELD_NAME, FIELD_VALUE);
  }

  @Test
  public void shoud_pass_if_both_are_null() throws Exception {
    Jedi jedi = new Jedi(null, "Blue");

    assertThat(jedi).hasField(FIELD_NAME, null);
  }

  @Test
  public void shoud_fail_if_field_not_exists() throws Exception {
    Jedi jedi = new Jedi("Yoda", "Blue");

    try {
      assertThat(jedi).hasField("not_exists_in_jedi_object", FIELD_VALUE);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\n"
                               + "Expecting\n"
                               + "  <Yoda the Jedi>\n"
                               + "to have field:\n"
                               + "  <\"not_exists_in_jedi_object\">");
    }
  }

  @Test
  public void shoud_fail_if_field_value_not_matches() throws Exception {
    Jedi jedi = new Jedi("Yoda", "Blue");

    try {
      assertThat(jedi).hasField(FIELD_NAME, 1000);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\n"
                               + "Expecting\n"
                               + "  <Yoda the Jedi>\n"
                               + "to have field:\n"
                               + "  <\"name\"> with value <1000>");
    }
  }

  @Test
  public void shoud_fail_if_field_value_not_matches_because_expected_is_null() throws Exception {
    Jedi jedi = new Jedi("Yoda", "Blue");

    try {
      assertThat(jedi).hasField(FIELD_NAME, null);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\n"
                               + "Expecting\n"
                               + "  <Yoda the Jedi>\n"
                               + "to have field:\n"
                               + "  <\"name\"> with value <null>");
    }
  }

  @Test
  public void shoud_fail_if_field_value_not_matches_because_object_field_is_null() throws Exception {
    Jedi jedi = new Jedi(null, "Blue");

    try {
      assertThat(jedi).hasField(FIELD_NAME, FIELD_VALUE);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\n"
                               + "Expecting\n"
                               + "  <null the Jedi>\n"
                               + "to have field:\n"
                               + "  <\"name\"> with value <\"Yoda\">");
    }
  }

  @Test
  public void shoud_fail_if_field_is_null() throws Exception {
    Jedi jedi = new Jedi("Yoda", "Blue");

    try {
      assertThat(jedi).hasField(null, FIELD_VALUE);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\n"
                               + "Expecting\n"
                               + "  <Yoda the Jedi>\n"
                               + "to have field:\n"
                               + "  <null>");
    }
  }
}
