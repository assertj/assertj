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
import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveFields;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.Person;
import org.junit.Test;

public class ShouldHaveFields_create_Test {

  @Test
  public void should_create_error_message_for_fields() {
    ErrorMessageFactory factory = shouldHaveFields(Person.class, newLinkedHashSet("name", "address"), newLinkedHashSet("address"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
                                  "[Test] %n"
                                      + "Expecting%n"
                                      + "  <org.assertj.core.test.Person>%n"
                                                + "to have the following public accessible fields:%n"
                                      + "  <[\"name\", \"address\"]>%n"
                                      + "but it doesn't have:%n"
                                      + "  <[\"address\"]>"));
  }
  
  @Test
  public void should_create_error_message_for_declared_fields() {
    ErrorMessageFactory factory = shouldHaveDeclaredFields(Person.class, newLinkedHashSet("name", "address"), newLinkedHashSet("address"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
                                  "[Test] %n"
                                      + "Expecting%n"
                                      + "  <org.assertj.core.test.Person>%n"
                                                + "to have the following declared fields:%n"
                                      + "  <[\"name\", \"address\"]>%n"
                                      + "but it doesn't have:%n"
                                      + "  <[\"address\"]>"));
  }
}
