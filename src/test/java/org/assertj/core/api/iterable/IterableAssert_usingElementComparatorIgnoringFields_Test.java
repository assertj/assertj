package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.Iterables;
import org.junit.Before;

public class IterableAssert_usingElementComparatorIgnoringFields_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;

  @Before
  public void before() {
	iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
	return assertions.usingElementComparatorIgnoringFields("field");
  }

  @Override
  protected void verify_internal_effects() {
	Iterables iterables = getIterables(assertions);
	assertThat(iterables).isNotSameAs(iterablesBefore);
	assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
	ComparatorBasedComparisonStrategy strategy = (ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy();
	assertThat(strategy.getComparator()).isInstanceOf(IgnoringFieldsComparator.class);
	assertThat(((IgnoringFieldsComparator) strategy.getComparator()).getFields()).containsOnly("field");
  }

}
