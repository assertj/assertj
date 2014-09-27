package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class OnFieldsComparator_creation_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_create_comparator_using_fields() {
	OnFieldsComparator comparator = new OnFieldsComparator("a", "b");
	assertThat(comparator).isNotNull();
	assertThat(comparator.getFields()).containsExactly("a", "b");
  }

  @Test
  public void should_fail_if_no_fields_are_given() {
	thrown.expectIllegalArgumentException("No fields specified");
	new OnFieldsComparator();
  }

  @Test
  public void should_fail_if_null_array_fields_is_given() {
	thrown.expectIllegalArgumentException("No fields specified");
	new OnFieldsComparator((String[]) null);
  }

  @Test
  public void should_fail_if_empty_array_fields_is_given() {
	thrown.expectIllegalArgumentException("No fields specified");
	new OnFieldsComparator(new String[0]);
  }

  @Test
  public void should_fail_if_some_fields_are_null() {
	thrown.expectIllegalArgumentException("Null/blank fields are invalid, fields were [\"a\", null]");
	new OnFieldsComparator("a", null);
  }
  
  @Test
  public void should_fail_if_some_fields_are_empty() {
	thrown.expectIllegalArgumentException("Null/blank fields are invalid, fields were [\"a\", \"\"]");
	new OnFieldsComparator("a", "");
  }
  
  @Test
  public void should_fail_if_some_fields_are_blank() {
	thrown.expectIllegalArgumentException("Null/blank fields are invalid, fields were [\"a\", \" \"]");
	new OnFieldsComparator("a", " ");
  }
  
}