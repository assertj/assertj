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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

/**
 * Tests for <code>{@link Collections#isNullOrEmpty(Collection)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Collections_isNullOrEmpty_Test {
  @Test
  public void should_return_true_if_Collection_is_empty() {
    Collection<String> c = new ArrayList<>();
    assertThat(Collections.isNullOrEmpty(c)).isTrue();
  }

  @Test
  public void should_return_true_if_Collection_is_null() {
    assertThat(Collections.isNullOrEmpty(null)).isTrue();
  }

  @Test
  public void should_return_false_if_Collection_has_elements() {
    Collection<String> c = newArrayList("Frodo");
    assertThat(Collections.isNullOrEmpty(c)).isFalse();
  }
}
