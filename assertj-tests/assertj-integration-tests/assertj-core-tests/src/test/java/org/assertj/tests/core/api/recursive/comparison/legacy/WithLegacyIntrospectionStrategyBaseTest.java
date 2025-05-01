package org.assertj.tests.core.api.recursive.comparison.legacy;

import org.assertj.core.api.recursive.comparison.LegacyRecursiveComparisonIntrospectionStrategy;
import org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_BaseTest;
import org.junit.jupiter.api.BeforeEach;

class WithLegacyIntrospectionStrategyBaseTest extends RecursiveComparisonAssert_BaseTest {

  @BeforeEach
  void beforeEachTest() {
    super.setup();
    recursiveComparisonConfiguration.setIntrospectionStrategy(new LegacyRecursiveComparisonIntrospectionStrategy());
  }
}
