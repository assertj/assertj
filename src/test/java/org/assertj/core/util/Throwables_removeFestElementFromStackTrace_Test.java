/*
 * Created on Dec 13, 2008
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
 * Copyright @2008-2011-2010 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for <code>{@link Throwables#removeAssertJRelatedElementsFromStackTrace(Throwable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Throwables_removeFestElementFromStackTrace_Test {
  @Test
  public void should_add_stack_trace_of_current_thread() {
    try {
      throw new FestThrowable();
    } catch (FestThrowable throwable) {
      assertTrue(hasStackTraceElementContainingAssertJClass(throwable));
      Throwables.removeAssertJRelatedElementsFromStackTrace(throwable);
      assertFalse(hasStackTraceElementContainingAssertJClass(throwable));
    }
  }

  private static boolean hasStackTraceElementContainingAssertJClass(FestThrowable throwable) {
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    for (StackTraceElement stackTraceElement : stackTrace) {
      if (stackTraceElement.getClassName().contains("org.assertj")) {
        return true;
      }
    }
    return false;
  }

  private static class FestThrowable extends Throwable {
    private static final long serialVersionUID = 1L;
  }
}
