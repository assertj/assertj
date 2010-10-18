/*
 * Created on Oct 18, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.error;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link ErrorWhenComparableIsNotLessThan#errorWhenNotLessThan(Comparable, Comparable)}</code>.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenComparableIsNotLessThan_errorWhenNotLessThan_Test {

  private static Integer actual;
  private static Integer other;

  @BeforeClass public static void setUpOnce() {
    actual = 8;
    other = 6;
  }

  @Test public void should_create_new_AssertionErrorFactory() {
    AssertionErrorFactory factory = ErrorWhenComparableIsNotLessThan.errorWhenNotLessThan(actual, other);
    assertEquals(ErrorWhenComparableIsNotLessThan.class, factory.getClass());
  }

  @Test public void should_pass_actual_and_other() {
    ErrorWhenComparableIsNotLessThan factory = (ErrorWhenComparableIsNotLessThan)
        ErrorWhenComparableIsNotLessThan.errorWhenNotLessThan(actual, other);
    assertSame(actual, factory.actual);
    assertSame(other, factory.other);
  }
}
