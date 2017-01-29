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
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class SoftAssertionErrorTest {

  @Test
  public void should_format_a_single_error_correctly() {
    SoftAssertionError error = new SoftAssertionError(Lists.newArrayList("One"));
    assertThat(error).hasMessage(format("%nThe following assertion failed:%n1) One%n"));
  }

  @Test
  public void should_format_multiple_errors_correctly() {
    SoftAssertionError error = new SoftAssertionError(Lists.newArrayList("One", "Two"));
    assertThat(error).hasMessage(format("%nThe following 2 assertions failed:%n1) One%n2) Two%n"));
  }

}
