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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Dan Corder
 */
public class ShouldNotBeEqualNormalizingWhitespace_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = shouldNotBeEqualNormalizingWhitespace(" my\tfoo bar ", " my  foo bar ");
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %n" +
                                                "Expecting:%n" +
                                                "  <\" my\tfoo bar \">%n" +
                                                "not to be equal to:%n" +
                                                "  <\" my  foo bar \">%n" +
                                                "after whitespace differences are normalized"));
  }
}
