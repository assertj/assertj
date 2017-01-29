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

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

/**
 * Tests for {@link Sets#newHashSet(Iterable)}.
 * 
 * @author Christian Rösch
 */
public class Sets_newHashSet_Iterable_Test {
  @Test
  public void should_return_Set_containing_iterable_elements() {
    String[] array = new String[] { "A", "b", "C" };
    Iterable<String> iterable = Arrays.asList(array);

    HashSet<String> set = Sets.newHashSet(iterable);
    assertThat(set).containsOnly(array);
  }

  @Test
  public void should_return_null_if_Iterable_is_null() {
    Iterable<String> iterable = null;
    assertThat(Sets.newHashSet(iterable)).isNull();
  }
}
