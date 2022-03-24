package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.spy;

public class IteratorsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected List<String> actual;
  protected Failures failures;
  protected Iterators iterators;
  protected AssertionInfo info;

  @BeforeEach
  public void setUp() {
    actual = newArrayList("Luke", "Yoda", "Leia");
    failures = spy(new Failures());
    iterators = new Iterators();
    iterators.failures = failures;
    info = someInfo();
  }

}
