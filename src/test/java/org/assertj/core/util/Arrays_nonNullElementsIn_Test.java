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

import static org.assertj.core.util.Arrays.nonNullElementsIn;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;

/**
 * Tests for <code>{@link Arrays#nonNullElementsIn(Object[])}</code>.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 */
public class Arrays_nonNullElementsIn_Test {
  @Test
  public void should_return_empty_Collection_if_given_array_is_null() {
    assertThat(Arrays.nonNullElementsIn(null)).isEmpty();
  }

  @Test
  public void should_return_an_empty_Collection_if_given_array_has_only_null_elements() {
    String[] array = new String[] { null };
    assertThat(Arrays.nonNullElementsIn(array)).isEmpty();
  }

  @Test
  public void should_return_an_empty_Collection_if_given_array_is_empty() {
    String[] array = new String[0];
    assertThat(Arrays.nonNullElementsIn(array)).isEmpty();
  }

  @Test
  public void should_return_a_Collection_without_null_elements() {
    String[] array = { "Frodo", null, "Sam", null };
    List<String> nonNull = nonNullElementsIn(array);
    assertThat(nonNull.toArray()).isEqualTo(new String[] { "Frodo", "Sam" });
  }
}
