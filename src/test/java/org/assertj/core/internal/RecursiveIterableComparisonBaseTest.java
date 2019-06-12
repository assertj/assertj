package org.assertj.core.internal;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceComparator;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.spy;

public class RecursiveIterableComparisonBaseTest {

  protected static ComparisonStrategy standartComparisonStrategy;
  protected static Failures failures;
  protected static Objects objects;
  protected static IterableElementComparisonStrategy iterableElementComparisonStrategy;
  protected static RecursiveComparisonDifferenceComparator recursiveComparisonDifferenceComparator;
  protected static RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  protected void setupBaseTest() {
    standartComparisonStrategy = new StandardComparisonStrategy();
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonDifferenceComparator = new RecursiveComparisonDifferenceComparator(recursiveComparisonConfiguration);
    iterableElementComparisonStrategy = new IterableElementComparisonStrategy<>(recursiveComparisonDifferenceComparator);
    objects = new Objects(iterableElementComparisonStrategy);
    failures = spy(new Failures());
    objects.failures = failures;
  }
}
