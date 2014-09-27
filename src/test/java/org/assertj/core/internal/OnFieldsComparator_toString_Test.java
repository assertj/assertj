package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class OnFieldsComparator_toString_Test {

  @Test
  public void should_return_description_for_multiple_given_fields() {
	assertThat(new OnFieldsComparator("a", "b").toString()).isEqualTo("field by field comparator on fields [\"a\", \"b\"]");
  }

  @Test
  public void should_return_description_for_a_single_given_field() {
	assertThat(new OnFieldsComparator("a").toString()).isEqualTo("single field comparator on field \"a\"");
  }
  
}