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

import com.google.common.collect.Multiset;

/**
 * Creates an error message stating that a given value appears in a {@link Multiset} fewer times than expected
 *
 * @author Max Daniline
 */
public class MultisetShouldContainAtLeastTimes extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldContainAtLeastTimes(final Multiset<?> actual, final Object expected,
                                                              final int expectedTimes, final int actualTimes) {
    return new MultisetShouldContainAtLeastTimes("%n" +
                                                 "Expecting:%n" +
                                                 "  %s%n" +
                                                 "to contain:%n" +
                                                 "  %s%n" +
                                                 "at least %s times but was found %s times.",
                                                 actual, expected, expectedTimes, actualTimes);
  }

  private MultisetShouldContainAtLeastTimes(String format, Object... arguments) {
    super(format, arguments);
  }

}
