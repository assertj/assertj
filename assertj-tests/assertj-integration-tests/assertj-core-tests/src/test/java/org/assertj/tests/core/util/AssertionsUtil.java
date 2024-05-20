/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.util;

import static java.nio.charset.Charset.forName;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.nio.charset.Charset;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.AssumptionViolatedException;

public class AssertionsUtil {

  public static final Charset TURKISH_CHARSET = forName("windows-1254");

  public static AssertionError expectAssertionError(ThrowingCallable shouldRaiseAssertionError) {
    AssertionError error = catchThrowableOfType(shouldRaiseAssertionError, AssertionError.class);
    assertThat(error).as("The code under test should have raised an AssertionError").isNotNull();
    return error;
  }

  public static ThrowableAssertAlternative<AssertionError> assertThatAssertionErrorIsThrownBy(ThrowingCallable shouldRaiseAssertionError) {
    return assertThatExceptionOfType(AssertionError.class).isThrownBy(shouldRaiseAssertionError);
  }

  public static void expectAssumptionNotMetException(ThrowingCallable shouldRaiseError) {
    assertThatThrownBy(shouldRaiseError).isInstanceOf(AssumptionViolatedException.class);
  }

  public static Charset getDifferentCharsetFrom(Charset charset) {
    return charset.equals(UTF_8) ? UTF_16 : UTF_8;
  }

}
