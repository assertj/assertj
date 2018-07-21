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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.booleanarray;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Comparator;

import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanArrayAssertBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link BooleanArrayAssert#usingElementComparator(Comparator)}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class BooleanArrayAssert_usingDefaultElementComparator_Test extends BooleanArrayAssertBaseTest {

  @Override
  @Test
  @SuppressWarnings("deprecation")
  public void should_have_internal_effects() {
    assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> assertions.usingDefaultElementComparator());
  }

  @Override
  @Test
  public void should_return_this() {
    // Disabled since this method throws an exception
  }

  @Override
  protected BooleanArrayAssert invoke_api_method() {
    // Not used in this test
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Not used in this test
  }

}
