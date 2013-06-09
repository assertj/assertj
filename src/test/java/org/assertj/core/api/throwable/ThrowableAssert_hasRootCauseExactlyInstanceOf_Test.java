package org.assertj.core.api.throwable;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;

/**
 * Tests for {@link org.assertj.core.api.ThrowableAssert#hasRootCauseExactlyInstanceOf(Class)}.
 * 
 * @author Jean-Christophe Gay
 */
public class ThrowableAssert_hasRootCauseExactlyInstanceOf_Test extends ThrowableAssertBaseTest {

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasRootCauseExactlyInstanceOf(Exception.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasRootCauseExactlyInstanceOf(getInfo(assertions), getActual(assertions), Exception.class);
  }
}
