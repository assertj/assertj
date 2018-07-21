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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeExactlyInstanceOf.shouldBeExactlyInstance;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.io.File;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeExactlyInstanceOf#create(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldBeExactlyInstance_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = shouldBeExactlyInstance("Yoda", File.class);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <\"Yoda\">%n" +
                                         "to be exactly an instance of:%n" +
                                         " <java.io.File>%n" +
                                         "but was an instance of:%n <java.lang.String>"));
  }

  @Test
  public void should_create_error_message_with_stack_trace_for_throwable() {
    IllegalArgumentException throwable = new IllegalArgumentException("Not a Nullpointer");
    String message = shouldBeExactlyInstance(throwable, NullPointerException.class).create();

    assertThat(message).isEqualTo(format("%nExpecting:%n" +
                                         " <java.lang.IllegalArgumentException: Not a Nullpointer>%n" +
                                         "to be exactly an instance of:%n" +
                                         " <java.lang.NullPointerException>%n" +
                                         "but was:%n" +
                                         " <\"%s\">",
                                         getStackTrace(throwable)));
  }
}
