package org.assertj.core.presentation;

import static org.assertj.core.api.BDDAssertions.then;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransformingListTest {
  @Test
  void should_handle_empty() {
    // GIVEN
    List<Integer> source = ImmutableList.of();
    List<String> transformed = new TransformingList<>(source, Object::toString);
    // WHEN/THEN
    then(transformed).isEmpty();
  }

  @Test
  void should_handle_non_empty() {
    // GIVEN
    List<Integer> source = ImmutableList.of(1, 2, 3);
    List<String> transformed = new TransformingList<>(source, Object::toString);
    // WHEN/THEN
    then(transformed).isEqualTo(ImmutableList.of("1", "2", "3"));
  }
}
