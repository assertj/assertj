/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Lists;
import org.junit.jupiter.api.BeforeEach;

public class ListAssert_usingElementComparatorIgnoringFields_Test extends ListAssertBaseTest {

  private Lists listsBefore;
  private Iterables iterablesBefore;

  @BeforeEach
  public void before() {
	listsBefore = getLists(assertions);
	iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ListAssert<String> invoke_api_method() {
	return assertions.usingElementComparatorIgnoringFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    Lists lists = getLists(assertions);
    Iterables iterables = getIterables(assertions);
    assertThat(lists).isNotSameAs(listsBefore);
    assertThat(iterables).isNotSameAs(iterablesBefore);
    assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    assertThat(lists.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    Comparator<?> listsElementComparator = ((ComparatorBasedComparisonStrategy) lists.getComparisonStrategy()).getComparator();
    assertThat(listsElementComparator).isInstanceOf(ExtendedByTypesComparator.class);
    assertThat(((IgnoringFieldsComparator) ((ExtendedByTypesComparator) listsElementComparator)
      .getComparator()).getFields()).containsOnly("field");
    Comparator<?> iterablesElementComparator = ((ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy()).getComparator();
    assertThat(iterablesElementComparator).isInstanceOf(ExtendedByTypesComparator.class);
    assertThat(((IgnoringFieldsComparator) ((ExtendedByTypesComparator) iterablesElementComparator)
      .getComparator()).getFields()).containsOnly("field");
  }

}
