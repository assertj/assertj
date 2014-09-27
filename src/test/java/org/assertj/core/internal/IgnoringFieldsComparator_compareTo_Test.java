package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IgnoringFieldsComparator_compareTo_Test {

  @Rule
  public ExpectedException thrown = none();

  private IgnoringFieldsComparator ignoringFieldsComparator;

  @Before
  public void setUp() {
	ignoringFieldsComparator = new IgnoringFieldsComparator("thinking");
  }

  @Test
  public void should_return_true_if_both_Objects_are_null() {
	assertThat(ignoringFieldsComparator.compare(null, null)).isZero();
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
	assertThat(ignoringFieldsComparator.compare(null, new DarthVader("I like you", "I'll kill you"))).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"), null)).isNotZero();
  }

  @Test
  public void should_return_true_if_all_but_ignored_fields_are_equal() {
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"),
	                                            new DarthVader("I like you", "I like you"))).isZero();
  }

  @Test
  public void should_return_false_if_all_but_ignored_fields_are_not_equal() {
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"),
	                                            new DarthVader("I'll kill you", "I'll kill you"))).isNotZero();
  }

  @Test
  public void should_throw_exception_if_Objects_have_not_the_same_properties() {
	thrown.expect(IllegalArgumentException.class);
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"), 2)).isNotZero();
  }

  public static class DarthVader {

	public final String telling;
	public final String thinking;

	public DarthVader(String telling, String thinking) {
	  this.telling = telling;
	  this.thinking = thinking;
	}

  }

}