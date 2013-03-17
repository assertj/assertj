/*
 * Created on Mar 17, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2012 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.error.ElementsShouldNotHaveAtMost.elementsShouldNotHaveAtMost;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ElementsShouldNotHaveAtMost;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ElementsShouldNotHaveAtMost#create(Description)}</code>.
 * 
 * @author Nicolas François
 */
public class ElementsShouldNotHaveAtMost_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = elementsShouldNotHaveAtMost(newArrayList("Yoda", "Luke", "Obiwan"), 2, new TestCondition<String>("Jedi power"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting elements:\n<['Yoda', 'Luke', 'Obiwan']>\n not to have at most 2 times <Jedi power>", message);
  }

}
