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

import com.google.common.base.Optional;

/**
 * Creates an error message indicating that an Optional which should be absent is actually present
 * 
 * @author Kornel Kiełczewski
 * @author Joel Costigliola
 */
public final class OptionalShouldBeAbsent extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory shouldBeAbsent(final Optional<T> actual) {
    return new OptionalShouldBeAbsent("Expecting Optional to contain nothing (absent Optional) but contained %s",
                                      actual.get());
  }

  private OptionalShouldBeAbsent(final String format, final Object... arguments) {
    super(format, arguments);
  }

}
