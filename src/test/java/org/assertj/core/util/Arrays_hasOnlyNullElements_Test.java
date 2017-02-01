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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link Arrays#hasOnlyNullElements(Object[])}</code>.
 * 
 * @author Alex Ruiz
 */
public class Arrays_hasOnlyNullElements_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_array_is_null() {
    thrown.expect(NullPointerException.class);
    Arrays.hasOnlyNullElements(null);
  }

  @Test
  public void should_return_true_if_array_has_only_null_elements() {
    String[] array = { null, null };
    assertThat(Arrays.hasOnlyNullElements(array)).isTrue();
  }

  @Test
  public void should_return_false_if_array_has_at_least_one_element_not_null() {
    String[] array = { null, "Frodo", null };
    assertThat(Arrays.hasOnlyNullElements(array)).isFalse();
  }

  @Test
  public void should_return_false_if_array_is_empty() {
    assertThat(Arrays.hasOnlyNullElements(new String[0])).isFalse();
  }
}
