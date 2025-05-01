package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;

public class RecursiveComparisonAssert_BaseTest {
  public static WritableAssertionInfo info = new WritableAssertionInfo();
  public RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  public static ComparisonDifference diff(List<String> path, Object actual, Object other) {
    return new ComparisonDifference(new DualValue(path, actual, other));
  }

  public static ComparisonDifference diff(List<String> path, Object actual, Object other, String additionalInformation) {
    return new ComparisonDifference(new DualValue(path, actual, other), additionalInformation);
  }

  public static ComparisonDifference diff(String path, Object actual, Object other) {
    return new ComparisonDifference(new DualValue(list(path), actual, other));
  }

  public static ComparisonDifference diff(String path, Object actual, Object other, String additionalInformation) {
    DualValue dualValue = new DualValue(list(path), actual, other);
    return new ComparisonDifference(dualValue, additionalInformation);
  }

  public static ComparisonDifference javaTypeDiff(String path, Object actual, Object other) {
    return RecursiveComparisonAssert_BaseTest.diff(path, actual, other, "Compared objects have java types and were thus compared with equals method");
  }

  public static ComparisonDifference javaTypeDiff(List<String> path, Object actual, Object other) {
    return RecursiveComparisonAssert_BaseTest.diff(path, actual, other, "Compared objects have java types and were thus compared with equals method");
  }

  public void compareRecursivelyFailsWithDifferences(Object actual, Object expected, ComparisonDifference... differences) {
    var recursiveComparisonAssert = new RecursiveComparisonAssert<>(actual, recursiveComparisonConfiguration);
    var errorMessageFactory = shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                              expected,
                                                                              list(differences),
                                                                              recursiveComparisonConfiguration,
                                                                              info.representation());
    var assertionError = expectAssertionError(() -> recursiveComparisonAssert.isEqualTo(expected));
    assertThat(assertionError).message().isEqualTo(errorMessageFactory.create());
  }
}
