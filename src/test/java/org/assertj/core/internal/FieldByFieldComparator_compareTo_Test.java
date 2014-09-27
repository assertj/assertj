package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FieldByFieldComparator_compareTo_Test {

  @Rule
  public ExpectedException thrown = none();

  private FieldByFieldComparator fieldByFieldComparator;

  @Before
  public void setUp() {
	fieldByFieldComparator = new FieldByFieldComparator();
  }

  @Test
  public void should_return_true_if_both_Objects_are_null() {
	assertThat(fieldByFieldComparator.compare(null, null)).isZero();
  }

  @Test
  public void should_return_true_if_Objects_are_equal() {
	assertThat(fieldByFieldComparator.compare(new JarJar("Yoda"), new JarJar("Yoda"))).isZero();
  }	

  @Test
  public void should_return_false_if_Objects_are_not_equal() {
	assertThat(fieldByFieldComparator.compare(new JarJar("Yoda"), new JarJar("HanSolo"))).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
	assertThat(fieldByFieldComparator.compare(null, new JarJar("Yoda"))).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
	assertThat(fieldByFieldComparator.compare(new JarJar("Yoda"), null)).isNotZero();
  }

  @Test
  public void should_throw_exception_if_Objects_have_not_the_same_properties() {
	thrown.expect(IllegalArgumentException.class);
	assertThat(fieldByFieldComparator.compare(new JarJar("Yoda"), 2)).isNotZero();
  }

  public static class JarJar {

	public final String field;

	public JarJar(String field) {
	  this.field = field;
	}

  }

}