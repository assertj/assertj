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
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstanceButWasNull;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.io.File;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeInstance#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeInstance_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeInstance("Yoda", File.class);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <\"Yoda\">%n" +
                                         "to be an instance of:%n" +
                                         "  <java.io.File>%n" +
                                         "but was instance of:%n" +
                                         "  <java.lang.String>"));
  }

  @Test
  public void should_create_error_message_with_stack_trace_for_throwable() {
    IllegalArgumentException throwable = new IllegalArgumentException("Not a file");
    String message = shouldBeInstance(throwable, File.class).create();

    assertThat(message).isEqualTo(format("%nExpecting:%n" +
                                         "  <java.lang.IllegalArgumentException: Not a file>%n" +
                                         "to be an instance of:%n" +
                                         "  <java.io.File>%n" +
                                         "but was:%n" +
                                         "  <\"%s\">",
                                         getStackTrace(throwable)));
  }

  @Test
  public void should_create_shouldBeInstanceButWasNull_error_message() {
    factory = shouldBeInstanceButWasNull("other", File.class);
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting object:%n" +
                                         "  \"other\"%n" +
                                         "to be an instance of:%n" +
                                         "  <java.io.File>%n" +
                                         "but was null"));
  }
}
