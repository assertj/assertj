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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.OnFieldsComparator;
import org.junit.Before;

public class IterableAssert_usingElementComparatorOnFields_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;

  @Before
  public void before() {
	iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
	return assertions.usingElementComparatorOnFields("field");
  }

  @Override
  protected void verify_internal_effects() {
	Iterables iterables = getIterables(assertions);
	assertThat(iterables).isNotSameAs(iterablesBefore);
	assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
	ComparatorBasedComparisonStrategy strategy = (ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy();
	assertThat(strategy.getComparator()).isInstanceOf(OnFieldsComparator.class);
	assertThat(((OnFieldsComparator)strategy.getComparator()).getFields()).containsOnly("field");
  }

}
