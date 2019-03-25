package org.assertj.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.time.temporal.Temporal;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;


public class TemporalAssert_usingComparator_Test extends AbstractTemporalAssertBaseTest {

  @Mock
  private Comparator<Temporal> comparator;

  @BeforeEach
  public void before() {
    initMocks(this);
  }

  @Override
  protected ConcreteTemporalAssert invoke_api_method() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    return assertions.usingComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(comparator).isSameAs(getComparables(assertions).getComparator());
    assertThat(comparator).isSameAs(getObjects(assertions).getComparator());
  }
}
