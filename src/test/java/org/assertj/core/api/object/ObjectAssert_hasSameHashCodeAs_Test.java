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

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;

/**
 * Tests for <code>{@link ObjectAssert#hasSameHashCodeAs(Object)}</code>.
 * 
 * @author Thomas Traude
 * @author Rudolf Klassen
 * @author Matthias Balke
 */
public class ObjectAssert_hasSameHashCodeAs_Test extends ObjectAssertBaseTest {

  public static final Jedi OTHER_JEDI = new Jedi("Yoda", "Green");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasSameHashCodeAs(OTHER_JEDI);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasSameHashCodeAs(getInfo(assertions), getActual(assertions), OTHER_JEDI);
  }

}
