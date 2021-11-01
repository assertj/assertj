package org.assertj.core.api.recursive.assertion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;

abstract class AbstractRecursiveAssertionDriverTestBase {

  protected Predicate<Object> succeedingMockPredicate;
  protected Predicate<Object> failingMockPredicate;

  Object emptyTestObject() {
    return new Object();
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  public void prepareMockPredicates() {
    succeedingMockPredicate = mock(Predicate.class, "call-verification-predicate-succeeding");
    when(succeedingMockPredicate.test(any())).thenReturn(true);

    failingMockPredicate = mock(Predicate.class, "call-verification-predicate-failing");
    when(failingMockPredicate.test(any())).thenReturn(false);
  }

  protected RecursiveAssertionDriver testSubjectWithDefaultConfiguration() {
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder().build();
    return new RecursiveAssertionDriver(configuration);
  }

}
