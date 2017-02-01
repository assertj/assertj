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
package org.assertj.core.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

import org.junit.Test;

public class Percentage_toString_Test {

  @Test
  public void toString_should_not_display_fractional_part_if_percentage_is_an_integer() {
    assertThat(withPercentage(10.0)).hasToString("10%");
    assertThat(withPercentage(10)).hasToString("10%");
  }

  @Test
  public void toString_should_display_fractional_part_if_percentage_is_a_double() {
    assertThat(withPercentage(0.1)).hasToString("0.1%");
    assertThat(withPercentage(0.103)).hasToString("0.103%");
  }
}
