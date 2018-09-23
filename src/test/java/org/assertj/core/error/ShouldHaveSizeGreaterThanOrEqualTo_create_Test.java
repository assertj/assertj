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
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveSizeGreaterThanOrEqualTo#create(Description, Representation)}</code>.
 *
 * @author Sandra Parsick
 * @author Georg Berky
 */
public class ShouldHaveSizeGreaterThanOrEqualTo_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  void setUp() {
    factory = shouldHaveSizeGreaterThanOrEqualTo("ab", 4, 2);
  }

  @Test
  void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %nExpecting size of:%n <2>%nto be greater than or equal to:<4> but was:<\"ab\">"));
  }

  @Test
  void should_create_error_message_with_hexadecimal_representation() {
    String message = factory.create(new TextDescription("Test"), new HexadecimalRepresentation());
    assertThat(message).isEqualTo(format("[Test] %nExpecting size of:%n <2>%nto be greater than or equal to:<4> but was:<\"['0x0061', '0x0062']\">"));
  }
}
