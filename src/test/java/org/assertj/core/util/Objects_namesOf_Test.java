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

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests for {@link Objects#namesOf(Class...)}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Objects_namesOf_Test {

  @Test
  public void should_return_empty_array_if_type_array_is_null() {
    assertThat(0).isEqualTo(Objects.namesOf((Class<?>[]) null).length);
  }

  @Test
  public void should_return_empty_array_if_type_array_is_empty() {
    assertThat(0).isEqualTo(Objects.namesOf(new Class<?>[0]).length);
  }

  @Test
  public void should_return_class_names() {
    String[] e = { String.class.getName(), Integer.class.getName() };
    String[] a = Objects.namesOf(String.class, Integer.class);
    assertThat(Arrays.equals(e, a))
        .as("expected:<%s> but got:<%s>", Arrays.toString(e), Arrays.toString(a))
        .isTrue();
  }
}
