package org.fest.assertions.api.string;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.StringAssert;
import org.fest.assertions.api.StringAssertBaseTest;


/**
 * Tests for <code>{@link StringAssert#containsOnlyOnce(String)}</code>.
 * 
 * @author Pauline Iogna
 * @author Joel Costigliola
 */
public class StringAssert_containsOnlyOnce_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    return assertions.containsOnlyOnce("od");
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertContainsOnlyOnce(getInfo(assertions), getActual(assertions), "od");
  }
}