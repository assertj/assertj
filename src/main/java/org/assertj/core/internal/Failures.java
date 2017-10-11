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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.MessageFormatter;
import org.assertj.core.error.ShouldBeEqual;
import org.assertj.core.util.Throwables;
import org.assertj.core.util.VisibleForTesting;

/**
 * Failure actions.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Failures {

  private static final String LINE_SEPARATOR = org.assertj.core.util.Compatibility.System.lineSeparator();

  private static final Failures INSTANCE = new Failures();

  /**
   * flag indicating that in case of a failure a threaddump is printed out.
   */
  private boolean printThreadDump = false;

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Failures instance() {
    return INSTANCE;
  }

  /**
   * flag indicating whether or not we remove elements related to AssertJ from assertion error stack trace.
   */
  private boolean removeAssertJRelatedElementsFromStackTrace = true;

  /**
   * Sets whether we remove elements related to AssertJ from assertion error stack trace.
   * 
   * @param removeAssertJRelatedElementsFromStackTrace flag
   */
  public void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    this.removeAssertJRelatedElementsFromStackTrace = removeAssertJRelatedElementsFromStackTrace;
  }

  /** 
   * Returns whether or not we remove elements related to AssertJ from assertion error stack trace. 
   * @return whether or not we remove elements related to AssertJ from assertion error stack trace.
   */
  public boolean isRemoveAssertJRelatedElementsFromStackTrace() {
    return removeAssertJRelatedElementsFromStackTrace;
  }

  @VisibleForTesting
  Failures() {}

  /**
   * Creates a <code>{@link AssertionError}</code> following this pattern:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code>
   * as the error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link AssertionErrorFactory}</code> to create an <code>{@link AssertionError}</code>,
   * prepending the value of <code>{@link AssertionInfo#description()}</code> to the error message</li>
   * </ol>
   * 
   * @param info contains information about the failed assertion.
   * @param factory knows how to create {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, AssertionErrorFactory factory) {
    AssertionError error = failureIfErrorMessageIsOverridden(info);
    if (error != null) return error;
    printThreadDumpIfNeeded();
    return factory.newAssertionError(info.description(), info.representation());
  }

  /**
   * Creates a <code>{@link AssertionError}</code> following this pattern:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code>
   * as the error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link ErrorMessageFactory}</code> to create the detail message of the
   * <code>{@link AssertionError}</code>, prepending the value of <code>{@link AssertionInfo#description()}</code> to
   * the error message</li>
   * </ol>
   * 
   * @param info contains information about the failed assertion.
   * @param message knows how to create detail messages for {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, ErrorMessageFactory message) {
    AssertionError error = failureIfErrorMessageIsOverridden(info);
    if (error != null) return error;
    AssertionError assertionError = new AssertionError(message.create(info.description(), info.representation()));
    removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    printThreadDumpIfNeeded();
    return assertionError;
  }

  public AssertionError failureIfErrorMessageIsOverridden(AssertionInfo info) {
    String overridingErrorMessage = info.overridingErrorMessage();
    return isNullOrEmpty(overridingErrorMessage) ? null
        : failure(MessageFormatter.instance().format(info.description(), info.representation(),
                                                     overridingErrorMessage));
  }

  /**
   * Creates a <code>{@link AssertionError}</code> using the given {@code String} as message.
   * <p>
   * It filters the AssertionError stack trace be default, to have full stack trace use
   * {@link #setRemoveAssertJRelatedElementsFromStackTrace(boolean)}.
   * 
   * @param message the message of the {@code AssertionError} to create.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(String message) {
    AssertionError assertionError = new AssertionError(message);
    removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    printThreadDumpIfNeeded();
    return assertionError;
  }

  /**
   * Creates a <code>{@link AssertionError}</code> for a {@link Throwable} class that was expected to be thrown.
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return the created <code>{@link AssertionError}</code>.
   * @since 2.6.0 / 3.6.0
   */
  public AssertionError expectedThrowableNotThrown(Class<? extends Throwable> throwableClass) {
    return failure(format("%s should have been thrown", throwableClass.getSimpleName()));
  }

  private void printThreadDumpIfNeeded() {
    if (printThreadDump) System.err.println(threadDumpDescription());
  }

  /**
   * If is {@link #removeAssertJRelatedElementsFromStackTrace} is true, it filters the stack trace of the given {@link AssertionError} 
   * by removing stack trace elements related to AssertJ in order to get a more readable stack trace.
   * <p>
   * See example below :
   * <pre><code class='java'> --------------- stack trace not filtered -----------------
  org.junit.ComparisonFailure: expected:&lt;'[Ronaldo]'&gt; but was:&lt;'[Messi]'&gt;
  at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
  at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
  at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
  at java.lang.reflect.Constructor.newInstance(Constructor.java:501)
  at org.assertj.core.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:34)
  at org.assertj.core.error.ShouldBeEqual.newComparisonFailure(ShouldBeEqual.java:111)
  at org.assertj.core.error.ShouldBeEqual.comparisonFailure(ShouldBeEqual.java:103)
  at org.assertj.core.error.ShouldBeEqual.newAssertionError(ShouldBeEqual.java:81)
  at org.assertj.core.internal.Failures.failure(Failures.java:76)
  at org.assertj.core.internal.Objects.assertEqual(Objects.java:116)
  at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:74)
  at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:13)
  
  --------------- stack trace filtered -----------------
  org.junit.ComparisonFailure: expected:&lt;'[Ronaldo]'&gt; but was:&lt;'[Messi]'&gt;
  at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
  at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
  at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
  at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:20)</code></pre>
   * 
   * Method is public because we need to call it from {@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)} that is building a junit ComparisonFailure by reflection.
   *  
   * @param assertionError the {@code AssertionError} to filter stack trace if option is set.
   */
  public void removeAssertJRelatedElementsFromStackTraceIfNeeded(AssertionError assertionError) {
    if (removeAssertJRelatedElementsFromStackTrace) {
      Throwables.removeAssertJRelatedElementsFromStackTrace(assertionError);
    }
  }

  /**
   * Set the flag indicating that in case of a failure a threaddump is printed out.
   */
  public void enablePrintThreadDump() {
    printThreadDump = true;
  }

  private String threadDumpDescription() {
    StringBuilder threadDumpDescription = new StringBuilder();
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
    for (ThreadInfo threadInfo : threadInfos) {
      threadDumpDescription.append(format("\"%s\"%n\tjava.lang.Thread.State: %s",
                                          threadInfo.getThreadName(), threadInfo.getThreadState()));
      for (StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
        threadDumpDescription.append(LINE_SEPARATOR).append("\t\tat ").append(stackTraceElement);
      }
      threadDumpDescription.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
    }
    return threadDumpDescription.toString();
  }
}
