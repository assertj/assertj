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
package org.assertj.core.util;

import static org.assertj.core.util.Lists.newArrayList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods related to <code>{@link Throwable}</code>s.
 * 
 * @author Alex Ruiz
 * @author Daniel Zlotin
 */
public final class Throwables {
  private static final String ORG_ASSERTJ_CORE_ERROR_CONSTRUCTOR_INVOKER = "org.assertj.core.error.ConstructorInvoker";
  private static final String JAVA_LANG_REFLECT_CONSTRUCTOR = "java.lang.reflect.Constructor";
  private static final String ORG_ASSERTJ = "org.assert";

  /**
   * Appends the stack trace of the current thread to the one in the given <code>{@link Throwable}</code>.
   * 
   * @param t the given {@code Throwable}.
   * @param methodToStartFrom the name of the method used as the starting point of the current thread's stack trace.
   */
  public static void appendStackTraceInCurrentThreadToThrowable(Throwable t, String methodToStartFrom) {
    List<StackTraceElement> stackTrace = newArrayList(t.getStackTrace());
    stackTrace.addAll(stackTraceInCurrentThread(methodToStartFrom));
    t.setStackTrace(stackTrace.toArray(new StackTraceElement[stackTrace.size()]));
  }

  private static List<StackTraceElement> stackTraceInCurrentThread(String methodToStartFrom) {
    List<StackTraceElement> filtered = stackTraceInCurrentThread();
    List<StackTraceElement> toRemove = new ArrayList<>();
    for (StackTraceElement e : filtered) {
      if (methodToStartFrom.equals(e.getMethodName())) {
        break;
      }
      toRemove.add(e);
    }
    filtered.removeAll(toRemove);
    return filtered;
  }

  private static List<StackTraceElement> stackTraceInCurrentThread() {
    return newArrayList(Thread.currentThread().getStackTrace());
  }

  /**
   * Removes the AssertJ-related elements from the <code>{@link Throwable}</code> stack trace that have little value for
   * end user. Therefore, instead of seeing this:
   * <pre><code class='java'> org.junit.ComparisonFailure: expected:&lt;'[Ronaldo]'&gt; but was:&lt;'[Messi]'&gt;
   *   at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
   *   at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
   *   at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
   *   at java.lang.reflect.Constructor.newInstance(Constructor.java:501)
   *   at org.assertj.core.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:34)
   *   at org.assertj.core.error.ShouldBeEqual.newComparisonFailure(ShouldBeEqual.java:111)
   *   at org.assertj.core.error.ShouldBeEqual.comparisonFailure(ShouldBeEqual.java:103)
   *   at org.assertj.core.error.ShouldBeEqual.newAssertionError(ShouldBeEqual.java:81)
   *   at org.assertj.core.internal.Failures.failure(Failures.java:76)
   *   at org.assertj.core.internal.Objects.assertEqual(Objects.java:116)
   *   at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:74)
   *   at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:13)</code></pre>
   *
   * We get this:
   * <pre><code class='java'> org.junit.ComparisonFailure: expected:&lt;'[Ronaldo]'&gt; but was:&lt;'[Messi]'&gt;
   *   at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
   *   at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
   *   at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
   *   at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:20)</code></pre>
   * @param throwable the {@code Throwable} to filter stack trace.
   */
  public static void removeAssertJRelatedElementsFromStackTrace(Throwable throwable) {
    List<StackTraceElement> filtered = newArrayList(throwable.getStackTrace());
    StackTraceElement previous = null;
    for (StackTraceElement element : throwable.getStackTrace()) {
      if (element.getClassName().contains(ORG_ASSERTJ)) {
        filtered.remove(element);
        // Handle the case when AssertJ builds a ComparisonFailure by reflection (see ShouldBeEqual.newAssertionError
        // method), the stack trace looks like:
        //
        // java.lang.reflect.Constructor.newInstance(Constructor.java:501),
        // org.assertj.core.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:34),
        //
        // We want to remove java.lang.reflect.Constructor.newInstance element because it is related to AssertJ.
        if (previous != null && JAVA_LANG_REFLECT_CONSTRUCTOR.equals(previous.getClassName())
            && element.getClassName().contains(ORG_ASSERTJ_CORE_ERROR_CONSTRUCTOR_INVOKER)) {
          filtered.remove(previous);
        }
      }
      previous = element;
    }
    StackTraceElement[] newStackTrace = filtered.toArray(new StackTraceElement[filtered.size()]);
    throwable.setStackTrace(newStackTrace);
  }


  /**
   * Get the root cause (ie the last non null cause) from a {@link Throwable}.
   * 
   * @param throwable the {@code Throwable} to get root cause from.
   * @return the root cause if any, else {@code null}.
   */
  public static Throwable getRootCause(Throwable throwable) {
    if (throwable.getCause() == null) return null;
    Throwable cause;
    while ((cause = throwable.getCause()) != null) throwable = cause;
    return throwable;
  }

  /**
   * Get the stack trace from a {@link Throwable} as a {@link String}.
   * 
   * <p>
   * The result of this method vary by JDK version as this method uses
   * {@link Throwable#printStackTrace(java.io.PrintWriter)}. On JDK1.3 and earlier, the cause exception will not be
   * shown unless the specified throwable alters printStackTrace.
   * </p>
   * 
   * @param throwable the {@code Throwable} to get stack trace from.
   * @return the stack trace as a {@link String}.
   */
  public static String getStackTrace(Throwable throwable) {
    StringWriter sw = null;
    PrintWriter pw = null;
    try {
      sw = new StringWriter();
      pw = new PrintWriter(sw, true);
      throwable.printStackTrace(pw);
      return sw.getBuffer().toString();
    } finally {
      Closeables.closeQuietly(sw, pw);
    }
  }

  private Throwables() {}
}
