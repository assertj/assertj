package org.assertj.core.api.throwable;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;

/**
 * Tests for {@link org.assertj.core.api.ThrowableAssert#hasRootCauseInstanceOf(Class)}.
 * 
 * @author Jean-Christophe Gay
 */
public class ThrowableAssert_hasRootCauseInstanceOf_Test extends ThrowableAssertBaseTest {

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasRootCauseInstanceOf(Exception.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasRootCauseInstanceOf(getInfo(assertions), getActual(assertions), Exception.class);
  }
}
