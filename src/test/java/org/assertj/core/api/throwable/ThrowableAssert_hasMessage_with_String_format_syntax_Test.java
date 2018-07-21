/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;
import org.junit.jupiter.api.Test;

public class ThrowableAssert_hasMessage_with_String_format_syntax_Test extends ThrowableAssertBaseTest {

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasMessage("throwable message %s", "foo");
  }

  @Override
  protected void verify_internal_effects() {
    // check that we end calling assertHasMessage with the resolved String.
    verify(throwables).assertHasMessage(getInfo(assertions), getActual(assertions), "throwable message foo");
  }

  @Test
  public void should_throw_if_String_format_syntax_is_not_met() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertions.hasMessage("throwable message %s %s %s", "foo"));
  }

}
