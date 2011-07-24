/*
 * Created on Mar 19, 2007
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2007-2011 the original author or authors.
 */
package org.fest.assertions.api;


/**
 * Common failures.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public final class Fail {

  /**
   * Fails with the given message.
   * @param failureMessage error message.
   * @return the thrown {@code AssertionError} .
   * @throws AssertionError with the given message.
   */
  public static void fail(String failureMessage) {
    throw new AssertionError(failureMessage);
  }
  
  /**
   * Throws an <code>{@link AssertionError}</code> with the given message and with the <code>{@link Throwable}</code>
   * that caused the failure.
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   */
  public static void fail(String failureMessage, Throwable realCause) {
    AssertionError error = new AssertionError(failureMessage);
    error.initCause(realCause);
    throw error;
  }

  /**
   * This constructor is protected to make it possible to subclass this class. Since all its methods are static, there
   * is no point on creating a new instance of it.
   */
  protected Fail() {}
}
