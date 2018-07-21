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

import static org.assertj.core.error.ElementsShouldHaveAtMost.elementsShouldHaveAtMost;
import static org.assertj.core.util.Lists.newArrayList;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link ElementsShouldHaveAtMost#create(Description)}</code>.
 * 
 * @author Nicolas François
 */
public class ElementsShouldHaveAtMost_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = elementsShouldHaveAtMost(newArrayList("Yoda", "Luke", "Obiwan"), 2, new TestCondition<String>("Jedi power"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting elements:%n<[\"Yoda\", \"Luke\", \"Obiwan\"]>%n to have at most 2 times <Jedi power>"
    ));
  }

}
