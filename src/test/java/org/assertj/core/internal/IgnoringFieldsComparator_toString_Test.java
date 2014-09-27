package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class IgnoringFieldsComparator_toString_Test {

  @Test
  public void should_return_description_of_IgnoringFieldsComparator() {
	assertThat(new IgnoringFieldsComparator("a", "b").toString()).isEqualTo("field by field comparator on all fields except [\"a\", \"b\"]");
  }

}