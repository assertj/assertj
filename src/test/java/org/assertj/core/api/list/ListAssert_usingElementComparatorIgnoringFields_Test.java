package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Lists;
import org.junit.Before;

public class ListAssert_usingElementComparatorIgnoringFields_Test extends ListAssertBaseTest {

  private Lists listsBefore;
  private Iterables iterablesBefore;

  @Before
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
	assertThat(listsElementComparator).isInstanceOf(IgnoringFieldsComparator.class);
	assertThat(((IgnoringFieldsComparator) listsElementComparator).getFields()).containsOnly("field");
	Comparator<?> iterablesElementComparator = ((ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy()).getComparator();
	assertThat(iterablesElementComparator).isInstanceOf(IgnoringFieldsComparator.class);
	assertThat(((IgnoringFieldsComparator) iterablesElementComparator).getFields()).containsOnly("field");
  }

}
