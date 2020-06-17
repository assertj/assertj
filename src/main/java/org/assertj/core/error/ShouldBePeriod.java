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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.time.Period;

/**
 * @author Hayden Meloche
 */
public class ShouldBePeriod extends BasicErrorMessageFactory {

  private static final String EXPECTED_PREFIX = "%nExpecting Period:%n  <%s>%nto be ";

  private ShouldBePeriod(Period actual, String metric) {
    super(EXPECTED_PREFIX + metric, actual);
  }

  public static ShouldBePeriod shouldBePositive(Period actual) {
    return new ShouldBePeriod(actual, "positive");
  }

  public static ShouldBePeriod shouldBeNegative(Period actual) {
    return new ShouldBePeriod(actual, "negative");
  }
}
