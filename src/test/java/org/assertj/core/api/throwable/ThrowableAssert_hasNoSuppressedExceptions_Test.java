/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;
import org.junit.Test;

/**
 * Tests for {@link org.assertj.core.api.ThrowableAssert#hasNoSuppressedExceptions()}.
 */
public class ThrowableAssert_hasNoSuppressedExceptions_Test extends ThrowableAssertBaseTest {

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasNoSuppressedExceptions();
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasNoSuppressedExceptions(getInfo(assertions), getActual(assertions));
  }

  @Test
  public void invoke_api_like_user() {
    assertThat(new Throwable()).hasNoSuppressedExceptions();
  }
}
