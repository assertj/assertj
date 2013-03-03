package org.fest.assertions.api;

import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * 
 * Base test class for all fest-guava-assert unit tests.
 *
 * @author Joel Costigliola
 *
 */
public class BaseTest {

  @Rule
  public ExpectedException thrown = none();

  public BaseTest() {
    super();
  }

  protected void expectException(Class<? extends Throwable> type, String message) {
    thrown.expect(type);
    thrown.expectMessage(message);
  }

}