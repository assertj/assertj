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

import static java.lang.Thread.currentThread;
import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Throwables#appendStackTraceInCurrentThreadToThrowable(Throwable, String)}.
 * 
 * @author Alex Ruiz
 */
public class Throwables_appendCurrentThreadStackTraceToThrowable_Test {
  private AtomicReference<RuntimeException> exceptionReference;

  @Before
  public void setUp() {
    exceptionReference = new AtomicReference<>();
  }

  @Test
  public void should_add_stack_trace_of_current_thread() {
    final CountDownLatch latch = new CountDownLatch(1);
    new Thread() {
      @Override
      public void run() {
        RuntimeException e = new RuntimeException("Thrown on purpose");
        exceptionReference.set(e);
        latch.countDown();
      }
    }.start();
    try {
      latch.await();
    } catch (InterruptedException e) {
      currentThread().interrupt();
    }
    RuntimeException thrown = exceptionReference.get();
    Throwables.appendStackTraceInCurrentThreadToThrowable(thrown, "should_add_stack_trace_of_current_thread");
    StackTraceElement[] stackTrace = thrown.getStackTrace();
    assertThat(asString(stackTrace[0])).isEqualTo(
        "org.assertj.core.util.Throwables_appendCurrentThreadStackTraceToThrowable_Test$1.run");
    assertThat(asString(stackTrace[1])).isEqualTo(
        "org.assertj.core.util.Throwables_appendCurrentThreadStackTraceToThrowable_Test.should_add_stack_trace_of_current_thread"
    );
  }

  private String asString(StackTraceElement e) {
    return concat(e.getClassName(), ".", e.getMethodName());
  }
}
