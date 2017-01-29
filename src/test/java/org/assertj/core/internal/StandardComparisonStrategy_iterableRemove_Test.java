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
package org.assertj.core.internal;

import static org.assertj.core.util.Lists.newArrayList;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link StandardComparisonStrategy#iterableRemoves(Iterable, Object)}.
 * 
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy_iterableRemove_Test extends AbstractTest_StandardComparisonStrategy {

  @Test
  public void should_pass() {
    List<?> list = newArrayList("Sam", "Merry", null, "Frodo");
    assertThat(list.contains("Frodo")).isTrue();
    standardComparisonStrategy.iterableRemoves(list, "Frodo");
    assertThat(list.contains("Frodo")).isFalse();
    standardComparisonStrategy.iterableRemoves(list, null);
    assertThat(list.contains(null)).isFalse();
  }

  @Test
  public void should_do_nothing_if_iterable_is_null() {
    standardComparisonStrategy.iterableRemoves(null, "Sauron");
  }

}
