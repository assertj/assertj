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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link ThrowableAssert#hasMessageMatching(String)}</code>.
 * 
 * @author Libor Ondrusek
 */
public class ThrowableAssert_hasMessageMatching_Test extends ThrowableAssertBaseTest {

  public static final String REGEX = "Given id='\\d{2,4}' not exists";

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasMessageMatching(REGEX);
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasMessageMatching(getInfo(assertions), getActual(assertions), REGEX);
  }

  @Test
  public void should_pass_if_matching() throws Exception {
    Throwable throwable = catchThrowable(raisingException("Given id='259' not exists"));

    assertThat(throwable).hasMessageMatching(REGEX);
  }

  @Test
  public void should_fail_if_not_matching() throws Exception {
    Throwable throwable = catchThrowable(raisingException("Given id='1' not exists"));

    try {
      assertThat(throwable).hasMessageMatching(REGEX);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%nExpecting message:%n <\"Given id='1' not exists\">%nto match regex:%n <\"Given id='\\d{2,4}' not exists\">%nbut did not."));
    }
  }

  @Test
  public void should_fail_if_regex_is_empty() throws Exception {
    Throwable throwable = catchThrowable(raisingException("Given id='1' not exists"));

    try {
      assertThat(throwable).hasMessageMatching("");
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%nExpecting message:%n <\"Given id='1' not exists\">%nto match regex:%n <\"\">%nbut did not."));
    }
  }

  @Test
  public void should_fail_if_regex_is_null() throws Exception {
    Throwable throwable = catchThrowable(raisingException("Given id='1' not exists"));

    try {
      assertThat(throwable).hasMessageMatching(null);
      failBecauseExceptionWasNotThrown(NullPointerException.class);
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("regex must not be null");
    }
  }

  @Test
  public void should_fail_if_throwable_hasnt_message() throws Exception {
    Throwable throwable = catchThrowable(raisingException(null));

    try {
      assertThat(throwable).hasMessageMatching(REGEX);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%nExpecting message:%n <null>%nto match regex:%n <\"Given id='\\d{2,4}' not exists\">%nbut did not."));
    }
  }
}
