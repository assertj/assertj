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
package org.assertj.core.util;

/**
 * Exception thrown by <code>{@link org.assertj.core.internal.Uris}</code>.
 *
 * @author Alexander Bischof
 */
public final class UrlsException extends RuntimeException {

  private static final long serialVersionUID = -8328554403430790831L;

  /**
   * Creates a new {@link org.assertj.core.util.UrlsException}.
   *
   * @param message the detail message.
   */
  public UrlsException(String message) {
    super(message);
  }

  /**
   * Creates a new {@link org.assertj.core.util.UrlsException}.
   *
   * @param message the detail message.
   * @param cause the cause of the error.
   */
  public UrlsException(String message, Throwable cause) {
    super(message, cause);
  }
}
