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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import java.math.BigDecimal;

public class ShouldHavePrecision extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHavePrecision(BigDecimal actual, int expectedPrecision) {
    return new ShouldHavePrecision(actual, expectedPrecision);
  }

  private ShouldHavePrecision(BigDecimal actual, int expectedPrecision) {
    super("%nExpecting %s to have a precision of:%n" +
          "  %s%n" +
          "but had a precision of:%n" +
          "  %s",
          actual, expectedPrecision, actual.precision());
  }
}
