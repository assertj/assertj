/*
 * Created on Nov 19, 2010
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
package org.fest.assertions.internal;

import static org.fest.assertions.error.DoesNotContainAtIndex.doesNotContainAtIndex;
import static org.fest.assertions.internal.CommonValidations.validateIndexValue;
import static org.fest.util.Objects.areEqual;

import java.util.List;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link List}</code>s.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Lists {

  private static final Lists INSTANCE = new Lists();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Lists instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private Lists() {
    this(Failures.instance());
  }

  @VisibleForTesting Lists(Failures failures) {
    this.failures = failures;
  }

  /**
   * Verifies that the given {@code List} contains the given object at the given index.
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given {@code List}.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   * the given {@code List}.
   * @throws AssertionError if the given {@code List} does not contain the given object at the given index.
   */
  public void assertContains(AssertionInfo info, List<?> actual, Object value, Index index) {
    assertNotNull(info, actual);
    Collections.instance().assertNotEmpty(info, actual);
    validateIndexValue(index, actual.size() - 1);
    Object actualElement = actual.get(index.value);
    if (areEqual(actualElement, value)) return;
    throw failures.failure(info, doesNotContainAtIndex(actual, value, index));
  }

  private void assertNotNull(AssertionInfo info, List<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
