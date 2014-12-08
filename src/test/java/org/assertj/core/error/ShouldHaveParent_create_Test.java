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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.mockito.Mockito.when;

/**
 * Tests for <code>{@link ShouldHaveParent#shouldHaveParent(java.io.File, java.io.File)}</code>
 *
 * @author Jean-Christophe Gay
 */
@RunWith(MockitoJUnitRunner.class)
public class ShouldHaveParent_create_Test {

  @Spy
  private File actual = new FakeFile("actual");

  private File expectedParent = new FakeFile("expected.parent");

  @Test
  public void should_create_error_message_when_actual_does_not_have_a_parent() {

    when(actual.getParentFile()).thenReturn(null);

    assertThat(createMessage()).isEqualTo(String.format("[TEST] %n" +
                                                        "Expecting file%n" +
                                                        "  <" + actual + ">%n" +
                                                        "to have parent:%n" +
                                                        "  <" + expectedParent + ">%n" +
                                                        "but did not have one."));
  }

  @Test
  public void should_create_error_message_when_actual_does_not_have_expected_parent() throws Exception {

    when(actual.getParentFile()).thenReturn(new FakeFile("not.expected.parent"));

    assertThat(createMessage()).isEqualTo(String.format("[TEST] %n" +
                                                        "Expecting file%n" +
                                                        "  <" + actual + ">%n" +
                                                        "to have parent:%n" +
                                                        "  <" + expectedParent + ">%n" +
                                                        "but had:%n" +
                                                        "  <" + actual.getParentFile() + ">."));
  }

  private String createMessage() {
    return shouldHaveParent(actual, expectedParent).create(new TestDescription("TEST"), new StandardRepresentation());
  }
}
