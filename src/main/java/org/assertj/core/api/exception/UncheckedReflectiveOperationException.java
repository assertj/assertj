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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.exception;

import static java.util.Objects.requireNonNull;

/**
 * Exception thrown if a reflective operation fails.
 *
 * @author Ashley Scopes
 */
public final class UncheckedReflectiveOperationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Initialize this exception from a {@link ReflectiveOperationException} of some description.
   *
   * @param cause   the ReflectiveOperationException that was thrown during a reflective operation.
   * @param message the descriptive message of what went wrong.
   */
  public UncheckedReflectiveOperationException(String message, ReflectiveOperationException cause) {
    super(
      requireNonNull(message, "message must not be null"),
      requireNonNull(cause, "cause must not be null")
    );
  }
}
