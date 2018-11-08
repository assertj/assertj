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
package org.assertj.core.api.objectarray;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

/**
 * Tests for <code>{@link ObjectArrayAssert#hasOnlyElementsOfTypes(Class...)} </code>.
 * 
 * @author Martin Winandy
 */
public class ObjectArrayAssert_hasOnlyElementsOfTypes_Test extends ObjectArrayAssertBaseTest {

  private final Class<?>[] types = { CharSequence.class };

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.hasOnlyElementsOfTypes(types);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasOnlyElementsOfTypes(getInfo(assertions), getActual(assertions), types);
  }

  @Test
  public void should_throw_assertion_error_and_not_null_pointer_exception_on_null() {
    // GIVEN
    Object[] array = array(null, "notNull");
    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(
      () -> assertThat(array).hasOnlyElementsOfTypes(types)
    )
      .withMessage(format(
        "%nExpecting:%n"
          + "  <[null, \"notNull\"]>%n"
          + "to only have instances of:%n"
          + "  <[java.lang.CharSequence]>%n"
          + "but these elements are not:%n"
          + "  <[null]>"
      ));
  }
}
