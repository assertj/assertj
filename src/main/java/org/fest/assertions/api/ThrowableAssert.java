/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this Throwable except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api;

import org.fest.assertions.internal.Throwables;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for <code>{@link Throwable}</code>s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Throwable)}</code>.
 * </p>
 * 
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ThrowableAssert extends AbstractAssert<ThrowableAssert, Throwable> {

  @VisibleForTesting
  Throwables throwables = Throwables.instance();

  protected ThrowableAssert(Throwable actual) {
    super(actual, ThrowableAssert.class);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} is equal to the given one.
   * @param message the expected message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   */
  public ThrowableAssert hasMessage(String message) {
    throwables.assertHasMessage(info, actual, message);
    return this;
  }

  /**
   * Verifies that the actual {@code Throwable} does not have a cause.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   */
  public ThrowableAssert hasNoCause() {
    throwables.assertHasNoCause(info, actual);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description.
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   */
  public ThrowableAssert hasMessageStartingWith(String description) {
    throwables.assertHasMessageStartingWith(info, actual, description);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains with the given description.
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public ThrowableAssert hasMessageContaining(String description) {
    throwables.assertHasMessageContaining(info, actual, description);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description.
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   */
  public ThrowableAssert hasMessageEndingWith(String description) {
    throwables.assertHasMessageEndingWith(info, actual, description);
    return this;
  }

}
