package org.assertj.core.api.throwable;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;

/**
 * Tests for {@link ThrowableAssert#hasCauseExactlyInstanceOf(Class)}.
 * 
 * @author Jean-Christophe Gay
 */
public class ThrowableAssert_hasCauseExactlyInstanceOf_Test extends ThrowableAssertBaseTest {

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasCauseExactlyInstanceOf(Exception.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasCauseExactlyInstanceOf(getInfo(assertions), getActual(assertions), Exception.class);
  }
}
