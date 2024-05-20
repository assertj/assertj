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

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.testkit.Jedi;

/**
 * Tests for <code>{@link ObjectAssert#hasFieldOrProperty(String)}</code>.
 *
 * @author Libor Ondrusek
 */
class ObjectAssert_hasFieldOrProperty_Test extends ObjectAssertBaseTest {

  public static final String FIELD_NAME = "name";

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasFieldOrProperty(FIELD_NAME);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasFieldOrProperty(getInfo(assertions), getActual(assertions), FIELD_NAME);
  }

}
