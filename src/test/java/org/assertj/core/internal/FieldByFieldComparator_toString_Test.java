package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FieldByFieldComparator_toString_Test {

  @Test
  public void should_return_description_of_FieldByFieldComparator() {
	assertThat(new FieldByFieldComparator().toString()).isEqualTo("field by field comparator on all fields");
  }

}