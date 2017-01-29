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

import java.util.LinkedHashSet;

import org.junit.Test;

/**
 * Tests for {@link Sets#newLinkedHashSet(Object...)}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Sets_newLinkedHashSet_GenericArray_Test {
  @Test
  public void should_return_Set_containing_all_elements_in_array() {
    String[] expected = { "One", "Two" };
    LinkedHashSet<String> set = Sets.newLinkedHashSet(expected);
    assertThat(set.toArray()).isEqualTo(expected);
  }

  @Test
  public void should_return_null_if_array_is_null() {
    Object[] elements = null;
    assertThat(Sets.newLinkedHashSet(elements)).isNull();
  }

  @Test
  public void should_return_empty_Set_if_array_is_empty() {
    LinkedHashSet<Object> set = Sets.newLinkedHashSet(new Object[0]);
    assertThat(set).isEmpty();
  }
}
