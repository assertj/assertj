/*
 * Created on Oct 23, 2010
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

import static java.lang.Character.*;

import static org.fest.assertions.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.fest.assertions.error.ShouldBeUpperCase.shouldBeUpperCase;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Character}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Characters extends Comparables {

  private static final Characters INSTANCE = new Characters();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Characters instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Characters() {
    super();
  }

  public Characters(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  /**
   * Asserts that the actual value is a lowercase character.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a lowercase character.
   */
  public void assertLowerCase(AssertionInfo info, Character actual) {
    assertNotNull(info, actual);
    if (isLowerCase(actual)) return;
    throw failures.failure(info, shouldBeLowerCase(actual));
  }

  /**
   * Asserts that the actual value is a uppercase character.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a uppercase character.
   */
  public void assertUpperCase(AssertionInfo info, Character actual) {
    assertNotNull(info, actual);
    if (isUpperCase(actual)) return;
    throw failures.failure(info, shouldBeUpperCase(actual));
  }
}
