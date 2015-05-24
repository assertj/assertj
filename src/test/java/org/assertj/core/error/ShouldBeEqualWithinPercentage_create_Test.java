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

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldBeEqualWithinPercentage#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alexander Bischof
 */
public class ShouldBeEqualWithinPercentage_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeEqualWithinPercentage(12.0, 10.0, withPercentage(10.0), 2d);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %n" +
                                  "Expecting:%n" +
                                  "  <12.0>%n" +
                                  "to be close to:%n" +
                                  "  <10.0>%n" +
                                  "by less than <10.0> percent but difference was <20.0> percent.%n" +
                                  "(a difference of exactly <10.0> being considered valid)"));
  }
}
