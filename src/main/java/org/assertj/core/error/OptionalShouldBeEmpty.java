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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.error.BasicErrorMessageFactory;

import java.util.Optional;

/**
 * Build error message when an {@link java.util.Optional} should be empty.
 *
 * @author Jean-Christophe Gay
 */
public class OptionalShouldBeEmpty extends BasicErrorMessageFactory {

  private OptionalShouldBeEmpty(Object optionalValue) {
	super("%nExpecting an empty Optional but was containing value: <%s>.", optionalValue);
  }

  /**
   * Indicates that the provided {@link java.util.Optional} should be empty.
   *
   * @param optional the actual {@link Optional} to test.
   * @param <T> the type of the value contained in the {@link java.util.Optional}.
   * @return a error message factory.
   */
  public static <T> OptionalShouldBeEmpty shouldBeEmpty(Optional<T> optional) {
	return new OptionalShouldBeEmpty(optional.get());
  }
}
