package org.assertj.core.presentation;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;

class PrimitiveArrayListTest {
  @Test
  void should_not_be_able_to_create_for_non_array() {
    // WHEN
    IllegalArgumentException exception = catchThrowableOfType(
      () -> new PrimitiveArrayList("not an array"), IllegalArgumentException.class);
    // THEN
    then(exception).hasMessageContaining("array");
  }

  @Test
  void should_handle_empty() {
    // GIVEN
    int[] array = new int[0];
    // WHEN
    List<Object> view = new PrimitiveArrayList(array);
    // THEN
    then(view).isEmpty();
  }

  @Test
  void should_handle_non_empty_primitive() {
    // GIVEN
    int[] array = new int[] {1, 2, 3};
    // WHEN
    List<Object> view = new PrimitiveArrayList(array);
    // THEN
    then(view).isEqualTo(ImmutableList.of(1, 2, 3));
  }

  @Test
  void should_handle_non_empty_objects() {
    // GIVEN
    Integer[] array = new Integer[] {1, 2, 3};
    // WHEN
    List<Object> view = new PrimitiveArrayList(array);
    // THEN
    then(view).isEqualTo(ImmutableList.of(1, 2, 3));
  }
}
