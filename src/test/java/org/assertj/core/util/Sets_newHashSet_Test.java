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

import java.util.HashSet;

import org.junit.Test;

/**
 * Tests for {@link Sets#newHashSet()}.
 * 
 * @author Christian Rösch
 */
public class Sets_newHashSet_Test {
  @Test
  public void should_return_empty_mutable_Set() {
    HashSet<Object> set = Sets.newHashSet();
    assertThat(set).isEmpty();

    set.add("element");
    assertThat(set).containsExactly("element");
  }

  @Test
  public void should_return_new_HashSet() {
    HashSet<Object> set1 = Sets.newHashSet();
    HashSet<Object> set2 = Sets.newHashSet();
    assertThat(set2).isNotSameAs(set1);

    // be sure they have nothing in common
    set1.add("element");
    assertThat(set2).isEmpty();
  }
}
