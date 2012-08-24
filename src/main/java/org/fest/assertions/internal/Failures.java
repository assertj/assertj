/*
 * Created on Aug 5, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.util.Strings.isNullOrEmpty;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.error.AssertionErrorFactory;
import org.fest.assertions.error.ErrorMessageFactory;
import org.fest.assertions.error.MessageFormatter;
import org.fest.assertions.error.ShouldBeEqual;
import org.fest.util.Throwables;
import org.fest.util.VisibleForTesting;

/**
 * Failure actions.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Failures {

  private static final Failures INSTANCE = new Failures();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Failures instance() {
    return INSTANCE;
  }

  /**
   * flag indicating wether or not we remove elements related to Fest from assertion error stack trace.
   */
  private boolean removeFestRelatedElementsFromStackTrace = true;

  /**
   * Sets wether we remove elements related to Fest from assertion error stack trace.
   * @param removeFestRelatedElementsFromStackTrace flag
   */
  public void setRemoveFestRelatedElementsFromStackTrace(boolean removeFestRelatedElementsFromStackTrace) {
    this.removeFestRelatedElementsFromStackTrace = removeFestRelatedElementsFromStackTrace;
  }

  @VisibleForTesting
  Failures() {}

  /**
   * Creates a <code>{@link AssertionError}</code> following this pattern:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code> as the
   * error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link AssertionErrorFactory}</code> to create an <code>{@link AssertionError}</code>, prepending
   * the value of <code>{@link AssertionInfo#description()}</code> to the error message</li>
   * </ol>
   * @param info contains information about the failed assertion.
   * @param factory knows how to create {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, AssertionErrorFactory factory) {
    AssertionError error = failureIfErrorMessageIsOverriden(info);
    if (error != null) return error;
    return factory.newAssertionError(info.description());
  }

  /**
   * Creates a <code>{@link AssertionError}</code> following this pattern:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code> as the
   * error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link ErrorMessageFactory}</code> to create the detail message of the
   * <code>{@link AssertionError}</code>, prepending the value of <code>{@link AssertionInfo#description()}</code> to the error
   * message</li>
   * </ol>
   * @param info contains information about the failed assertion.
   * @param message knows how to create detail messages for {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, ErrorMessageFactory message) {
    AssertionError error = failureIfErrorMessageIsOverriden(info);
    if (error != null) return error;
    AssertionError assertionError = new AssertionError(message.create(info.description()));
    removeFestRelatedElementsFromStackTraceIfNeeded(assertionError);
    return assertionError;
  }

  private AssertionError failureIfErrorMessageIsOverriden(AssertionInfo info) {
    String overridingErrorMessage = info.overridingErrorMessage();
    return isNullOrEmpty(overridingErrorMessage) ? null : failure(MessageFormatter.instance().format(info.description(),
        overridingErrorMessage));
  }

  /**
   * Creates a <code>{@link AssertionError}</code> using the given {@code String} as message.
   * <p>
   * It filters the AssertionError stack trace be default, to have full stack trace use
   * {@link #setRemoveFestRelatedElementsFromStackTrace(boolean)}.
   * @param message the message of the {@code AssertionError} to create.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(String message) {
    AssertionError assertionError = new AssertionError(message);
    removeFestRelatedElementsFromStackTraceIfNeeded(assertionError);
    return assertionError;
  }

/**
   * If is {@link #removeFestRelatedElementsFromStackTrace} is true, it filters the stack trace of the given {@link AssertionError} 
   * by removing stack trace elements related to Fest in order to get a more readable stack trace.
   * <p>
   * See example below :
   * <pre>
--------------- stack trace not filtered -----------------
org.junit.ComparisonFailure: expected:<'[Ronaldo]'> but was:<'[Messi]'>
  at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
  at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
  at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
  at java.lang.reflect.Constructor.newInstance(Constructor.java:501)
  at org.fest.assertions.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:34)
  at org.fest.assertions.error.ShouldBeEqual.newComparisonFailure(ShouldBeEqual.java:111)
  at org.fest.assertions.error.ShouldBeEqual.comparisonFailure(ShouldBeEqual.java:103)
  at org.fest.assertions.error.ShouldBeEqual.newAssertionError(ShouldBeEqual.java:81)
  at org.fest.assertions.internal.Failures.failure(Failures.java:76)
  at org.fest.assertions.internal.Objects.assertEqual(Objects.java:116)
  at org.fest.assertions.api.AbstractAssert.isEqualTo(AbstractAssert.java:74)
  at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:13)
  
--------------- stack trace filtered -----------------
org.junit.ComparisonFailure: expected:<'[Ronaldo]'> but was:<'[Messi]'>
  at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
  at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
  at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
  at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:20)
   * </pre>
   * 
   * Method is public because we need to call it from {@link ShouldBeEqual#newAssertionError(org.fest.assertions.description.Description)} that is building a junit ComparisonFailure by reflection. 
   *  
   * @param assertionError the {@code AssertionError} to filter stack trace if option is set.
   */
  public void removeFestRelatedElementsFromStackTraceIfNeeded(AssertionError assertionError) {
    if (removeFestRelatedElementsFromStackTrace) {
      Throwables.removeFestRelatedElementsFromStackTrace(assertionError);
    }
  }

}
