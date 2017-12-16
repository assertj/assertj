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
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.internal.TestDescription;
import org.junit.Test;

public class ShouldNotBeEqualWithinOffset_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeEqual(8f, 6f, offset(5f), 2f);
    // WHEN
    String message = factory.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <8.0f>%n" +
                                         "not to be close to:%n" +
                                         "  <6.0f>%n" +
                                         "by less than <5.0f> but difference was <2.0f>.%n" +
                                         "(a difference of exactly <5.0f> being considered invalid)"));
  }

  @Test
  public void should_create_error_message_for_strict_offset() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotBeEqual(8f, 6f, byLessThan(5f), 2f);
    // WHEN
    String message = factory.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <8.0f>%n" +
                                         "not to be close to:%n" +
                                         "  <6.0f>%n" +
                                         "by less than <5.0f> but difference was <2.0f>.%n" +
                                         "(a difference of exactly <5.0f> being considered valid)"));
  }
}
