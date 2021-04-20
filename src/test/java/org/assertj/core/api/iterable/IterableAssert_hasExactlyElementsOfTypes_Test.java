package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.internal.ObjectArrays;
import org.junit.jupiter.api.Test;

/**
 * Only make one test since assertion is delegated to {@link ObjectArrays} which has its own tests.
 */
class IterableAssert_hasExactlyElementsOfTypes_Test
{
  @Test
  void should_pass_if_actual_has_elements_of_the_expected_types_in_order()
  {
    List<Object> list = newArrayList(1, "a", 1.00);
    assertThat(list).hasExactlyElementsOfTypes(Integer.class, String.class, Double.class);
  }
}
