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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Arrays#isNullOrEmpty(Object[])}</code>.
 * 
 * @author Alex Ruiz
 */
public class Arrays_isNullOrEmpty_Test {
  @Test
  public void should_return_true_if_array_is_empty() {
    assertThat(Arrays.isNullOrEmpty(new String[0])).isTrue();
  }

  @Test
  public void should_return_true_if_array_is_null() {
    assertThat(Arrays.isNullOrEmpty(null)).isTrue();
  }

  @Test
  public void should_return_false_if_array_has_elements() {
    assertThat(Arrays.isNullOrEmpty(new String[] { "Tuzi" })).isFalse();
  }
}
