/*
 * Created on Apr 23, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2012 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.error.ShouldBeLenientEqualByAccepting.shouldBeLenientEqualByAccepting;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeLenientEqualByIgnoring;
import org.assertj.core.test.Jedi;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeLenientEqualByIgnoring#create(Description)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldBeLenientEqualByAccepting_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message_with_all_fields_differences() {
    factory = shouldBeLenientEqualByAccepting(new Jedi("Luke", "blue"), newArrayList("name", "lightSaberColor"),
        newArrayList((Object) "Yoda", (Object) "green"), newArrayList("name", "lightSaberColor"));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expected values:\n" + "<['Yoda', 'green']>\n" + " in fields:\n"
        + "<['name', 'lightSaberColor']>\n" + " of <Luke the Jedi>.\n"
        + "Comparison was performed on fields <['name', 'lightSaberColor']>", message);
  }

  @Test
  public void should_create_error_message_with_single_field_difference() {
    factory = shouldBeLenientEqualByAccepting(new Jedi("Yoda", "green"), newArrayList("lightSaberColor"),
        newArrayList((Object) "green"), newArrayList("lightSaberColor"));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expected value <'green'> in field <'lightSaberColor'> of <Yoda the Jedi>.\n"
        + "Comparison was performed on fields <['lightSaberColor']>", message);
  }

}
