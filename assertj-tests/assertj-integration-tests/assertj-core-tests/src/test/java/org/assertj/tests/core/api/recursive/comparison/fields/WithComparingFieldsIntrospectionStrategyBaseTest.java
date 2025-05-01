package org.assertj.tests.core.api.recursive.comparison.fields;

import org.assertj.core.api.recursive.comparison.ComparingFields;
import org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_BaseTest;
import org.junit.jupiter.api.BeforeEach;

class WithComparingFieldsIntrospectionStrategyBaseTest extends RecursiveComparisonAssert_BaseTest {

  @BeforeEach
  void beforeEachTest() {
    super.setup();
    recursiveComparisonConfiguration.setIntrospectionStrategy(new ComparingFields());
  }
}
