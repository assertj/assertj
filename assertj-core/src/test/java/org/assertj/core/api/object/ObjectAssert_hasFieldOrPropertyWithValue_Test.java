/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#hasFieldOrPropertyWithValue(String, Object)}</code>.
 *
 * @author Libor Ondrusek
 */
class ObjectAssert_hasFieldOrPropertyWithValue_Test extends ObjectAssertBaseTest {

  public static final String FIELD_NAME = "name"; // field in org.assertj.core.testkit.Person
  public static final String FIELD_VALUE = "Yoda"; // field value in org.assertj.core.testkit.Person

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
  void should_pass_if_both_are_null() {
    Jedi jedi = new Jedi(null, "Blue");

    assertThat(jedi).hasFieldOrPropertyWithValue(FIELD_NAME, null);
  }

}
