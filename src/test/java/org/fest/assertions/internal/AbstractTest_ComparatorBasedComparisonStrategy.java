package org.fest.assertions.internal;

import static org.junit.rules.ExpectedException.none;

import java.util.Comparator;

import org.fest.assertions.internal.ComparatorBasedComparisonStrategy;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class AbstractTest_ComparatorBasedComparisonStrategy {

  @Rule
  public ExpectedException thrown = none();
  protected Comparator<String> caseInsensitiveStringComparator = CaseInsensitiveStringComparator.instance;
  protected ComparatorBasedComparisonStrategy caseInsensitiveComparisonStrategy = new ComparatorBasedComparisonStrategy(
      caseInsensitiveStringComparator);

}