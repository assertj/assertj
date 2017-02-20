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
package org.assertj.core.error;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Build error message when an {@link Optional}, {@link OptionalDouble}, {@link OptionalInt} or {@link OptionalLong}
 * should contain a specific value.
 *
 * @author Jean-Christophe Gay
 * @author Nicolai Parlog
 * @author Grzegorz Piwowarek
 *
 */
public class OptionalShouldContain extends BasicErrorMessageFactory {

  private static final String EXPECTING_TO_CONTAIN = "%nExpecting:%n  <%s>%nto contain:%n  <%s>%nbut did not.";
  private static final String EXPECTING_TO_CONTAIN_SAME = "%nExpecting:%n  <%s>%nto contain the instance (i.e. compared with ==):%n  <%s>%nbut did not.";

  private OptionalShouldContain(String message, Object actual, Object expected) {
    super(message, actual, expected);
  }

  private OptionalShouldContain(Object expected) {
    super("%nExpecting Optional to contain:%n  <%s>%nbut was empty.", expected);
  }

  /**
   * Indicates that the provided {@link java.util.Optional} does not contain the provided argument.
   *
   * @param optional the {@link java.util.Optional} which contains a value.
   * @param expectedValue the value we expect to be in the provided {@link java.util.Optional}.
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   * @return a error message factory
   */
  public static <VALUE> OptionalShouldContain shouldContain(Optional<VALUE> optional, VALUE expectedValue) {
    return optional.isPresent() ?
            new OptionalShouldContain(EXPECTING_TO_CONTAIN, optional, expectedValue) :
            shouldContain(expectedValue);
  }

  /**
   * Indicates that the provided {@link java.util.OptionalDouble} does not contain the provided argument.
   *
   * @param optional the {@link java.util.OptionalDouble} which contains a value.
   * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalDouble}.
   * @return a error message factory
   */
  public static OptionalShouldContain shouldContain(OptionalDouble optional, double expectedValue) {
    return optional.isPresent() ?
            new OptionalShouldContain(EXPECTING_TO_CONTAIN, optional, expectedValue) :
            shouldContain(expectedValue);
  }

  /**
   * Indicates that the provided {@link java.util.OptionalInt} does not contain the provided argument.
   *
   * @param optional the {@link java.util.OptionalInt} which contains a value.
   * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalInt}.
   * @return a error message factory
   */
  public static OptionalShouldContain shouldContain(OptionalInt optional, int expectedValue) {
    return optional.isPresent() ?
            new OptionalShouldContain(EXPECTING_TO_CONTAIN, optional, expectedValue) :
            shouldContain(expectedValue);
  }

  /**
   * Indicates that the provided {@link java.util.OptionalLong} does not contain the provided argument.
   *
   * @param optional the {@link java.util.OptionalLong} which contains a value.
   * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalLong}.
   * @return a error message factory
   */
  public static OptionalShouldContain shouldContain(OptionalLong optional, long expectedValue) {
    return optional.isPresent() ?
            new OptionalShouldContain(EXPECTING_TO_CONTAIN, optional, expectedValue) :
            shouldContain(expectedValue);
  }

  /**
   * Indicates that the provided {@link java.util.Optional} does not contain the provided argument (judging by reference
   * equality).
   *
   * @param optional the {@link java.util.Optional} which contains a value.
   * @param expectedValue the value we expect to be in the provided {@link java.util.Optional}.
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   * @return a error message factory
   */
  public static <VALUE> OptionalShouldContain shouldContainSame(Optional<VALUE> optional, VALUE expectedValue) {
    return optional.isPresent() ?
            new OptionalShouldContain(EXPECTING_TO_CONTAIN_SAME, optional, expectedValue) :
            shouldContain(expectedValue);
  }

  /**
   * Indicates that an {@link java.util.Optional} is empty so it doesn't contain the expected value.
   *
   * @param expectedValue the value we expect to be in an {@link java.util.Optional}.
   * @return a error message factory.
   */
  public static OptionalShouldContain shouldContain(Object expectedValue) {
    return new OptionalShouldContain(expectedValue);
  }
}
