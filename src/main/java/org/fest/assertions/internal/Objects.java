/*
 * Created on Aug 4, 2010
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

import static org.fest.assertions.error.ErrorWhenNotEqualFactory.errorWhenNotEqual;
import static org.fest.util.Objects.areEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for {@code Object}s.
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Objects {

  private static final Objects INSTANCE = new Objects();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Objects instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private Objects() {
    this(Failures.instance());
  }

  @VisibleForTesting Objects(Failures failures) {
    this.failures = failures;
  }

  /**
   * Asserts that two objects are equal.
   * @param info contains information about the assertion.
   * @param expected the expected value.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, Object expected, Object actual) {
    if (areEqual(expected, actual)) return;
    throw failures.failure(info, errorWhenNotEqual(expected, actual));
  }
}
