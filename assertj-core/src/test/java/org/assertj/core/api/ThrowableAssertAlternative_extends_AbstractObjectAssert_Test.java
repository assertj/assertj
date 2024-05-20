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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThrowableAssertAlternative")
class ThrowableAssertAlternative_extends_AbstractObjectAssert_Test {

  @Test
  void should_allow_chaining_object_assertions() {
    // GIVEN
    Throwable authenticationException = new AuthenticationException(503);
    // WHEN/THEN
    thenExceptionOfType(AuthenticationException.class).isThrownBy(() -> throwing(authenticationException))
                                                      .returns(503, from(AuthenticationException::getStatusCode));
  }

  static void throwing(Throwable t) throws Throwable {
    throw t;
  }

  private static class AuthenticationException extends Exception {
    private static final long serialVersionUID = 1L;
    private int statusCode = 1;

    public AuthenticationException(int statusCode) {
      this.statusCode = statusCode;
    }

    public int getStatusCode() {
      return statusCode;
    }
  }

}
