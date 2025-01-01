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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util.introspection;

import java.util.Optional;

/**
 * Error that occurred when using <a href="http://java.sun.com/docs/books/tutorial/javabeans/introspection/index.html">JavaBeans
 * Introspection</a>.
 * 
 * @author Alex Ruiz
 */
public class IntrospectionError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * This (nullable) field holds the original Exception thrown by the tested code
   * during the invocation of a getter/accessor method. This allows us to reference
   * or rethrow it if alternative means of resolving the field are unsuccessful.
   */
  private final Throwable getterInvocationException;

  /**
   * Creates a new <code>{@link IntrospectionError}</code>.
   * @param message the detail message.
   */
  public IntrospectionError(String message) {
    super(message);
    this.getterInvocationException = null;
  }

  /**
   * Creates a new <code>{@link IntrospectionError}</code>.
   * @param message the detail message.
   * @param cause the original cause.
   */
  public IntrospectionError(String message, Throwable cause) {
    this(message, cause, null);
  }

  /**
   * Creates a new <code>{@link IntrospectionError}</code>.
   * @param message the detail message.
   * @param cause the original cause.
   * @param getterInvocationException the original exception thrown by the tested code.
   */
  public IntrospectionError(String message, Throwable cause, Throwable getterInvocationException) {
    super(message, cause);
    this.getterInvocationException = getterInvocationException;
  }

  public Optional<Throwable> getterInvocationException() {
    return Optional.ofNullable(getterInvocationException);
  }
}
