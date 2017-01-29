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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;

import org.assertj.core.internal.TestDescription;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldBeEqualWithinPercentage#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alexander Bischof
 */
public class ShouldBeEqualWithinPercentage_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message_with_int_percentage_displayed_as_int() {
    factory = shouldBeEqualWithinPercentage(12.0, 10.0, withPercentage(10), 2d);
    String message = factory.create(new TestDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <12.0>%n" +
                                         "to be close to:%n" +
                                         "  <10.0>%n" +
                                         "by less than 10%% but difference was 20.0%%.%n" +
                                         "(a difference of exactly 10%% being considered valid)"));
  }

  @Test
  public void should_create_error_message_with_double_percentage_displayed_as_int() {
    factory = shouldBeEqualWithinPercentage(12.0, 10.0, withPercentage(10.0), 2d);
    String message = factory.create(new TestDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <12.0>%n" +
                                         "to be close to:%n" +
                                         "  <10.0>%n" +
                                         "by less than 10%% but difference was 20.0%%.%n" +
                                         "(a difference of exactly 10%% being considered valid)"));
  }

  @Test
  public void should_create_error_message_with_percentage_as_double() {
    factory = shouldBeEqualWithinPercentage(12.0, 10.0, withPercentage(0.5), 2d);
    String message = factory.create(new TestDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <12.0>%n" +
                                         "to be close to:%n" +
                                         "  <10.0>%n" +
                                         "by less than 0.5%% but difference was 20.0%%.%n" +
                                         "(a difference of exactly 0.5%% being considered valid)"));
  }
}
