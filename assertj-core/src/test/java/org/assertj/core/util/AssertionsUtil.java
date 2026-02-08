/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

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
import org.assertj.core.error.MultipleAssertionsError;
import org.jspecify.annotations.NonNull;
import org.opentest4j.TestAbortedException;

public class AssertionsUtil {

  public static final Charset TURKISH_CHARSET = forName("windows-1254");

  public static AssertionError expectAssertionError(ThrowingCallable shouldRaiseAssertionError) {
    AssertionError error = catchThrowableOfType(AssertionError.class, shouldRaiseAssertionError);
    assertThat(error).as("The code under test should have raised an AssertionError").isNotNull();
    return error;
  }

  public static @NonNull MultipleAssertionsError expectMultipleAssertionsError(ThrowingCallable callable) {
    return (MultipleAssertionsError) expectAssertionError(callable);
  }



  public static ThrowableAssertAlternative<AssertionError> assertThatAssertionErrorIsThrownBy(ThrowingCallable shouldRaiseAssertionError) {
    return assertThatExceptionOfType(AssertionError.class).isThrownBy(shouldRaiseAssertionError);
  }

  public static void expectAssumptionNotMetException(ThrowingCallable shouldRaiseError) {
    assertThatThrownBy(shouldRaiseError).isInstanceOf(TestAbortedException.class);
  }

  public static Charset getDifferentCharsetFrom(Charset charset) {
    return charset.equals(UTF_8) ? UTF_16 : UTF_8;
  }

}
