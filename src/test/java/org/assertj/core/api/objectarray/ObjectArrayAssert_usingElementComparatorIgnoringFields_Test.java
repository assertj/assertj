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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.ObjectArrays;
import org.junit.Before;

public class ObjectArrayAssert_usingElementComparatorIgnoringFields_Test extends ObjectArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @Before
  public void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
	return assertions.usingElementComparatorIgnoringFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    ObjectArrays iterables = getArrays(assertions);
    assertThat(iterables).isNotSameAs(arraysBefore);
	assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
	ComparatorBasedComparisonStrategy strategy = (ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy();
	assertThat(strategy.getComparator()).isInstanceOf(IgnoringFieldsComparator.class);
	assertThat(((IgnoringFieldsComparator) strategy.getComparator()).getFields()).containsOnly("field");
  }

}
