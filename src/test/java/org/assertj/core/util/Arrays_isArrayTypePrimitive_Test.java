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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.isArrayTypePrimitive;

import org.junit.jupiter.api.Test;

public class Arrays_isArrayTypePrimitive_Test {

  @Test
  public void should_return_true_if_object_is_a_primitive_array() {
    // GIVEN
    int[] o = new int[0];
    // WHEN
    boolean isArrayTypePrimitive = isArrayTypePrimitive(o);
    // THEN
    then(isArrayTypePrimitive).isTrue();
  }

  @Test
  public void should_return_false_if_object_is_an_object_array() {
    // GIVEN
    Object[] o = new Object[0];
    // WHEN
    boolean isArrayTypePrimitive = isArrayTypePrimitive(o);
    // THEN
    then(isArrayTypePrimitive).isFalse();
  }

  @Test
  public void should_return_false_if_object_is_null() {
    // GIVEN
    Object o = null;
    // WHEN
    boolean isArrayTypePrimitive = isArrayTypePrimitive(o);
    // THEN
    then(isArrayTypePrimitive).isFalse();
  }

  @Test
  public void should_return_false_if_object_is_not_an_array() {
    // GIVEN
    String string = "I'm not an array";
    // WHEN
    boolean isArrayTypePrimitive = isArrayTypePrimitive(string);
    // THEN
    then(isArrayTypePrimitive).isFalse();
  }
}
