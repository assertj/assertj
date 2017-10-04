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
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.OnFieldsComparator;
import org.junit.Before;

public class ListAssert_usingElementComparatorOnFields_Test extends ListAssertBaseTest {

  private Lists listsBefore;
  private Iterables iterablesBefore;

  @Before
  public void before() {
	listsBefore = getLists(assertions);
	iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ListAssert<String> invoke_api_method() {
	return assertions.usingElementComparatorOnFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    Lists lists = getLists(assertions);
    Iterables iterables = getIterables(assertions);
    assertThat(lists).isNotSameAs(listsBefore);
    assertThat(iterables).isNotSameAs(iterablesBefore);
    assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    assertThat(lists.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    Comparator<?> listsElementComparator = ((ExtendedByTypesComparator) ((ComparatorBasedComparisonStrategy)
      lists.getComparisonStrategy()).getComparator()).getComparator();
    assertThat(listsElementComparator).isInstanceOf(OnFieldsComparator.class);
    assertThat(((OnFieldsComparator) listsElementComparator).getFields()).containsOnly("field");
    Comparator<?> iterablesElementComparator = ((ExtendedByTypesComparator) ((ComparatorBasedComparisonStrategy)
      iterables.getComparisonStrategy()).getComparator()).getComparator();
    assertThat(iterablesElementComparator).isInstanceOf(OnFieldsComparator.class);
    assertThat(((OnFieldsComparator) iterablesElementComparator).getFields()).containsOnly("field");
  }

}
