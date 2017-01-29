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
import static org.assertj.core.error.ShouldNotBeInstance.shouldNotBeInstance;
import static org.assertj.core.util.Throwables.getStackTrace;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldNotBeInstance#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldNotBeInstance_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldNotBeInstance("Yoda", String.class);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <\"Yoda\">%n" +
                                         "not to be an instance of:<java.lang.String>"));
  }

  @Test
  public void should_create_error_message_with_stack_trace_for_throwable() {
    IllegalArgumentException throwable = new IllegalArgumentException();
    String message = shouldNotBeInstance(throwable, IllegalArgumentException.class).create();

    assertThat(message).isEqualTo(format("%nExpecting:%n" +
                                         " <\"" + getStackTrace(throwable) + "\">%n" +
                                         "not to be an instance of:<java.lang.IllegalArgumentException>"));
  }
}
