package org.assertj.core.api.recursive.comparison.iterables;

import org.assertj.core.api.RecursiveIterableComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

public class RecursiveIterableComparisonAssert_isEqualTo_ignoringCollectionOrder_Test extends RecursiveIterableComparisonAssert_isEqualTo_BaseTest {

  @Test
  public void should_pass_when_iterables_contain_same_items_in_different_order_and_order_is_ignored() {
    // GIVE
    List<Person> actual = list(sheldon, penny, raj, howard);
    Set<Person> expected = new HashSet<>(actual);
    // THEN
    assertThat(actual).usingRecursiveComparison()
      .ignoringCollectionOrder()
      .isEqualTo(expected);
  }
}
