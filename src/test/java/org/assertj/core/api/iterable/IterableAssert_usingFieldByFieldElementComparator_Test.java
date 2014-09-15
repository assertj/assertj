package org.assertj.core.api.iterable;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.junit.Before;
import org.junit.Test;

public class IterableAssert_usingFieldByFieldElementComparator_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;

  @Before
  public void before() {
	iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
	return assertions.usingFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
	assertNotSame(getIterables(assertions), iterablesBefore);
	assertTrue(getIterables(assertions).getComparisonStrategy() instanceof ComparatorBasedComparisonStrategy);
	assertTrue(getObjects(assertions).getComparisonStrategy() instanceof IterableElementComparisonStrategy);
  }

  @Test
  public void succesful_isEqualTo_assertion_using_field_by_field_element_comparator() {
	List<Foo> list1 = singletonList(new Foo("id", 1));
	List<Foo> list2 = singletonList(new Foo("id", 1));
	assertThat(list1).usingFieldByFieldElementComparator().isEqualTo(list2);
  }

  @Test
  public void succesful_isIn_assertion_using_field_by_field_element_comparator() {
	List<Foo> list1 = singletonList(new Foo("id", 1));
	List<Foo> list2 = singletonList(new Foo("id", 1));
	System.out.println(new FieldByFieldComparator());
	assertThat(list1).usingFieldByFieldElementComparator().isIn(singletonList(list2));
  }

  @Test
  public void failed_isEqualTo_assertion_using_field_by_field_element_comparator() {
	List<Foo> list1 = singletonList(new Foo("id", 1));
	List<Foo> list2 = singletonList(new Foo("id", 2));
	try {
	  assertThat(list1).usingFieldByFieldElementComparator().isEqualTo(list2);
	} catch (AssertionError e) {
	  assertThat(e).hasMessage("\nExpecting:\n" +
		                       " <[Foo(id=id, bar=1)]>\n" +
		                       "to be equal to:\n" +
		                       " <[Foo(id=id, bar=2)]>\n" +
		                       "when comparing elements using 'field by field comparator on all fields' but was not.");
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void failed_isIn_assertion_using_field_by_field_element_comparator() {
	List<Foo> list1 = singletonList(new Foo("id", 1));
	List<Foo> list2 = singletonList(new Foo("id", 2));
	try {
	  assertThat(list1).usingFieldByFieldElementComparator().isIn(singletonList(list2));
	} catch (AssertionError e) {
	  assertThat(e).hasMessage("\nExpecting:\n" +
		                       " <[Foo(id=id, bar=1)]>\n" +
		                       "to be in:\n" +
		                       " <[[Foo(id=id, bar=2)]]>\n" +
		                       "when comparing elements using 'field by field comparator on all fields'");
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  public static class Foo {
	public final String id;
	public final int bar;

	public Foo(final String id, final int bar) {
	  this.id = id;
	  this.bar = bar;
	}

	@Override
	public String toString() {
	  return "Foo(id=" + id + ", bar=" + bar + ")";
	}

  }
}
