/*
 * Created on Mar 19, 2007
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2007-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static java.lang.String.format;

import org.fest.assertions.internal.Failures;

/**
 * Common failures.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public final class Fail {

  /**
   * Sets wether we remove elements related to Fest from assertion error stack trace.
   * @param removeFestRelatedElementsFromStackTrace flag.
   */
  public static void setRemoveFestRelatedElementsFromStackTrace(boolean removeFestRelatedElementsFromStackTrace) {
    Failures.instance().setRemoveFestRelatedElementsFromStackTrace(removeFestRelatedElementsFromStackTrace);
  }

  /**
   * Fails with the given message.
   * @param failureMessage error message.
   * @throws AssertionError with the given message.
   */
  public static void fail(String failureMessage) {
    throw Failures.instance().failure(failureMessage);
  }

  /**
   * Throws an {@link AssertionError} with the given message and with the {@link Throwable} that caused the failure.
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   */
  public static void fail(String failureMessage, Throwable realCause) {
    AssertionError error = Failures.instance().failure(failureMessage);
    error.initCause(realCause);
    throw error;
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  public static void failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
    throw Failures.instance().failure(format("Expected %s to be thrown", throwableClass.getSimpleName()));
  }

  /**
   * This constructor is protected to make it possible to subclass this class. Since all its methods are static, there is no point
   * on creating a new instance of it.
   */
  protected Fail() {}
}
