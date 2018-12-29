package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newTreeSet;

import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class ComparisonDifference_compareTo_Test {

  @Test
  public void should_order_differences_by_alphabetical_path_ignoring_dot_separator() {
    // GIVEN
    ComparisonDifference diff1 = comparisonDifference("a", "b");
    ComparisonDifference diff2 = comparisonDifference("a", "c");
    ComparisonDifference diff3 = comparisonDifference("aa");
    ComparisonDifference diff4 = comparisonDifference("a", "b", "c");
    ComparisonDifference diff5 = comparisonDifference("b");
    ComparisonDifference diff6 = comparisonDifference("aaa");
    // WHEN
    Set<ComparisonDifference> differences = newTreeSet(diff1, diff2, diff3, diff4, diff5, diff6);
    // THEN
    assertThat(differences).extracting(ComparisonDifference::getPath)
                           .containsExactly("aa", "aaa", "a.b", "a.b.c", "a.c", "b");
  }

  private static ComparisonDifference comparisonDifference(String... pathElements) {
    return new ComparisonDifference(list(pathElements), RandomStringUtils.random(5), RandomStringUtils.random(6));
  }

}
