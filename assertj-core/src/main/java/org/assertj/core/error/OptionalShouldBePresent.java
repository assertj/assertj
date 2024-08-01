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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

/**
 * Build error message when a value should be present in an {@link java.util.Optional}.
 *
 * @author Jean-Christophe Gay
 */
public class OptionalShouldBePresent extends BasicErrorMessageFactory {

  private OptionalShouldBePresent(Object optional) {
    super(format("%nExpecting %s to contain a value but it was empty.", optional.getClass().getSimpleName()));
  }

  /**
   * Indicates that a value should be present in an empty {@link java.util.Optional}.
   *
   * @param optional the optional instance
   * @return an error message factory.
   * @throws java.lang.NullPointerException if optional is null.
   */
  public static OptionalShouldBePresent shouldBePresent(Object optional) {
    return new OptionalShouldBePresent(optional);
  }
}
