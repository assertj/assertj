package org.assertj.core.api.string;

import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;

import static org.mockito.Mockito.verify;



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