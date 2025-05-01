package org.assertj.tests.core.api.recursive.comparison.properties;

import org.assertj.core.api.recursive.comparison.ComparingProperties;
import org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_BaseTest;
import org.junit.jupiter.api.BeforeEach;

class WithComparingPropertiesIntrospectionStrategyBaseTest extends RecursiveComparisonAssert_BaseTest {

  @BeforeEach
  void beforeEachTest() {
    super.setup();
    recursiveComparisonConfiguration.setIntrospectionStrategy(new ComparingProperties());
  }
}
