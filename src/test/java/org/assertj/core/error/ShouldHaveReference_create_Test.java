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
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldHaveReference.shouldHaveReference;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import org.assertj.core.internal.TestDescription;
import org.junit.Test;

public class ShouldHaveReference_create_Test {

  private static final TestDescription TEST_DESCRIPTION = new TestDescription("TEST");

  @Test
  public void should_create_error_message_for_AtomicMarkableReference() throws Exception {
    // GIVEN
    AtomicMarkableReference<String> actual = new AtomicMarkableReference<>("foo", true);
    // WHEN
    String message = shouldHaveReference(actual, actual.getReference(), "bar").create(TEST_DESCRIPTION,
                                                                                      CONFIGURATION_PROVIDER.representation());
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting%n" +
                                         "  <AtomicMarkableReference[marked=true, reference=\"foo\"]>%n" +
                                         "to have reference:%n" +
                                         "  <\"bar\">%n" +
                                         "but had:%n" +
                                         "  <\"foo\">"));
  }

  @Test
  public void should_create_error_message_for_AtomicStampedReference() throws Exception {
    // GIVEN
    AtomicStampedReference<String> actual = new AtomicStampedReference<>("foo", 123);
    // WHEN
    String message = shouldHaveReference(actual, actual.getReference(), "bar").create(TEST_DESCRIPTION,
                                                                                      CONFIGURATION_PROVIDER.representation());
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting%n" +
                                         "  <AtomicStampedReference[stamp=123, reference=\"foo\"]>%n" +
                                         "to have reference:%n" +
                                         "  <\"bar\">%n" +
                                         "but had:%n" +
                                         "  <\"foo\">"));
  }

}
