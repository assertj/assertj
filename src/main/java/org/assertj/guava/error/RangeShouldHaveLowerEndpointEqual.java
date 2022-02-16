/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.Range;

public class RangeShouldHaveLowerEndpointEqual extends BasicErrorMessageFactory {

  public static <T extends Comparable<T>> ErrorMessageFactory shouldHaveEqualLowerEndpoint(final Range<T> actual,
                                                                                           final Object value) {
    return new RangeShouldHaveLowerEndpointEqual("%n" +
                                                 "Expecting:%n" +
                                                 "  %s%n" +
                                                 "to have lower endpoint equal to:%n" +
                                                 "  %s%n" +
                                                 "but was:%n" +
                                                 "  %s",
                                                 actual, value, actual.lowerEndpoint());
  }

  /**
   * Creates a new <code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   *
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  private RangeShouldHaveLowerEndpointEqual(final String format, final Object... arguments) {
    super(format, arguments);
  }
}
