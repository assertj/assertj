package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FieldByFieldComparisonStrategy_areEqual_Test {

  @Rule
  public ExpectedException thrown = none();

  private FieldByFieldComparisonStrategy fieldByFieldComparisonStrategy;

  @Before
  public void setUp() {
	fieldByFieldComparisonStrategy = new FieldByFieldComparisonStrategy();
  }

  @Test
  public void should_return_true_if_both_Objects_are_null() {
	assertThat(fieldByFieldComparisonStrategy.areEqual(null, null)).isTrue();
  }

  @Test
  public void should_return_true_if_Objects_are_equal() {
	assertThat(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), new JarJar("Yoda"))).isTrue();
  }	

  @Test
  public void should_return_false_if_Objects_are_not_equal() {
	assertThat(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), new JarJar("HanSolo"))).isFalse();
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
	assertThat(fieldByFieldComparisonStrategy.areEqual(null, new JarJar("Yoda"))).isFalse();
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
	assertThat(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), null)).isFalse();
  }

  @Test
  public void should_throw_exception_if_Objects_have_not_the_same_properties() {
	thrown.expect(IllegalArgumentException.class);
	assertThat(fieldByFieldComparisonStrategy.areEqual(new JarJar("Yoda"), 2)).isFalse();
  }

  public static class JarJar {

	public final String field;

	public JarJar(String field) {
	  this.field = field;
	}

  }

}