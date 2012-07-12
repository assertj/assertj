package org.fest.assertions.api;

import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.*;

import org.fest.assertions.internal.Dates;

/**
 * 
 * Abstract base of all DateAssert tests.
 * 
 * @author Joel Costigliola
 * 
 */
public abstract class AbstractBaseDateAssert_Test {

  protected DateAssert assertions;
  protected Dates dates;

  @Before
  public void setUp() {
    dates = mock(Dates.class);
    assertions = new DateAssert(new Date());
    assertions.dates = dates;
  }

}
