/*
 * Created on Jun 2, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.*;
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
    assertTrue(Arrays.hasOnlyNullElements(array));
  }

  @Test
  public void should_return_false_if_array_has_at_least_one_element_not_null() {
    String[] array = { null, "Frodo", null };
    assertFalse(Arrays.hasOnlyNullElements(array));
  }

  @Test
  public void should_return_false_if_array_is_empty() {
    assertFalse(Arrays.hasOnlyNullElements(new String[0]));
  }
}
