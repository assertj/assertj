/*
 * Created on May 16, 2007
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2007-2011 the original author or authors.
 */
package org.assertj.core.internal;

/**
 * Exception thrown by <code>{@link org.assertj.core.internal.Readers}</code>.
 *
 * @author Matthieu Baechler
 * @author Bartosz Bierkowski
 */
public class ReadersException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates a new <code>{@link org.assertj.core.internal.ReadersException}</code>.
   * 
   * @param message the detail message.
   */
  public ReadersException(String message) {
    super(message);
  }

  /**
   * Creates a new <code>{@link org.assertj.core.internal.ReadersException}</code>.
   * 
   * @param message the detail message.
   * @param cause the cause of the error.
   */
  public ReadersException(String message, Throwable cause) {
    super(message, cause);
  }
}