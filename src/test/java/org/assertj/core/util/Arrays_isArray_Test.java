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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for <code>{@link Arrays#isArray(Object)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Arrays_isArray_Test {

  @Test
  public void should_return_true_if_object_is_an_array() {
    assertThat(Arrays.isArray(new String[0])).isTrue();
    assertThat(Arrays.isArray(new int[0])).isTrue();
  }

  @Test
  public void should_return_false_if_object_is_null() {
    assertThat(Arrays.isArray(null)).isFalse();
  }

  @Test
  public void should_return_false_if_object_is_not_an_array() {
    assertThat(Arrays.isArray("I'm not an array")).isFalse();
  }
}
