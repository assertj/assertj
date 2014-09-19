package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.internal.ObjectArrays;
import org.junit.Test;

/**
 * Only make one test since assertion is delegated to {@link ObjectArrays} which has its own tests.
 */
public class IterableAssert_hasOnlyElementsOfType_Test {

  @Test
  public void should_pass_if_actual_has_one_element_of_the_expected_type() {
	List<Integer> list = newArrayList(1, 2);
	assertThat(list).hasOnlyElementsOfType(Number.class).hasOnlyElementsOfType(Integer.class);
  }

}