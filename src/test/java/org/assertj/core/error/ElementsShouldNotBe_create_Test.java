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

import static org.assertj.core.error.ElementsShouldNotBe.elementsShouldNotBe;
import static org.assertj.core.util.Lists.newArrayList;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ElementsShouldNotBe#create(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Nicolas François
 */
public class ElementsShouldNotBe_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = elementsShouldNotBe(newArrayList("Darth Vader", "Leia", "Yoda"), newArrayList("Yoda"), new TestCondition<String>("not a Jedi"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting elements:%n<[\"Yoda\"]>%n of %n<[\"Darth Vader\", \"Leia\", \"Yoda\"]>%n not to be <not a Jedi>"
    ));
  }

}
