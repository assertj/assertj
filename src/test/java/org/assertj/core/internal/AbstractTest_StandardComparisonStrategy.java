package org.assertj.core.internal;

import static org.junit.rules.ExpectedException.none;

import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class AbstractTest_StandardComparisonStrategy {

  @Rule
  public ExpectedException thrown = none();
  protected static StandardComparisonStrategy standardComparisonStrategy = StandardComparisonStrategy.instance();

}