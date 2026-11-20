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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrayAssert#contains(Object...)}</code>.
 * 
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
class ObjectArrayAssert_contains_Test extends ObjectArrayAssertBaseTest {

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.contains("Yoda", "Luke");
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContains(getInfo(assertions), getActual(assertions), array("Yoda", "Luke"));
  }

  @Test
  void should_disambiguate_enum_elements_of_a_self_referencing_array() {
    // GIVEN
    Object[] actual = new Object[2];
    actual[0] = actual;
    actual[1] = org.assertj.core.internal.objects.pkg1.Example.VALUE;
    var expected = org.assertj.core.internal.objects.pkg2.Example.VALUE;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(error).hasMessageContaining("(this array)")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg1.Example@")
               .hasMessageContaining("VALUE (org.assertj.core.internal.objects.pkg2.Example@");
  }
}
