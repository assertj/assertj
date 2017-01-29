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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.*;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.*;

/**
 * Tests for <code>{@link ShouldHaveSameSizeAs#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldHaveSameSizeAs_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldHaveSameSizeAs(newArrayList('a', 'b'), 2, 4);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
     assertThat(message).isEqualTo(String.format("[Test] %n" +
                                   "Actual and expected should have same size but actual size is:%n" +
                                   " <2>%n" +
                                   "while expected is:%n" +
                                   " <4>%n" +
                                   "Actual was:%n" +
                                   "<['a', 'b']>"));
  }

  @Test
  public void should_create_error_message_with_hexadecimal_representation() {
    String message = factory.create(new TextDescription("Test"), new HexadecimalRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %n" +
                                  "Actual and expected should have same size but actual size is:%n" +
                                  " <2>%n" +
                                  "while expected is:%n" +
                                  " <4>%n" +
                                  "Actual was:%n<['0x0061', '0x0062']>"));
}
}
