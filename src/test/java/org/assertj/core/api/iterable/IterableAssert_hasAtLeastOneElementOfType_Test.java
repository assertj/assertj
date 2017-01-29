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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.internal.ObjectArrays;
import org.junit.Test;

/**
 * Only make one test since assertion is delegated to {@link ObjectArrays} which has its own tests.
 */
public class IterableAssert_hasAtLeastOneElementOfType_Test {

  @Test
  public void should_pass_if_actual_has_one_element_of_the_expected_type() {
	List<Object> list = newArrayList();
	list.add("string");
	list.add(1);
	assertThat(list).hasAtLeastOneElementOfType(Integer.class)
	                .hasAtLeastOneElementOfType(String.class)
	                .hasAtLeastOneElementOfType(Object.class);
  }

}