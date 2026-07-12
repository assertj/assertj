/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.Range;

/**
 * Creates an error message indicating that a range should have the expected upper endpoint.
 */
public class RangeShouldHaveUpperEndpointEqual extends BasicErrorMessageFactory {

  /**
   * Creates an error message for a range without the expected upper endpoint.
   *
   * @param <T> the type of values in the range
   * @param actual the actual range
   * @param value the expected upper endpoint
   * @return the created error message factory
   */
  public static <T extends Comparable<T>> ErrorMessageFactory shouldHaveEqualUpperEndpoint(final Range<T> actual,
                                                                                           final Object value) {
    return new RangeShouldHaveUpperEndpointEqual("%n" +
                                                 "Expecting:%n" +
                                                 "  %s%n" +
                                                 "to have upper endpoint equal to:%n" +
                                                 "  %s%n" +
                                                 "but was:%n" +
                                                 "  %s",
                                                 actual, value, actual.upperEndpoint());
  }

  /**
   * Creates a new <code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   *
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  private RangeShouldHaveUpperEndpointEqual(final String format, final Object... arguments) {
    super(format, arguments);
  }
}
