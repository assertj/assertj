/*
 * Created on Mar 19, 2012
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

import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.test.Jedi;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeEqualToIgnoringFields#create(Description)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ShouldBeEqualIgnoringGivenFields_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message_with_all_fields_differences() {
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "green"), newArrayList("name", "lightSaberColor"),
                                                 newArrayList((Object) "Yoda", "green"), newArrayList("lightSaberColor"));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting values:\n  <['Yoda', 'green']>\nin fields:\n  <['name', 'lightSaberColor']>\n"
        + "of <Yoda the Jedi>.\n" + "Comparison was performed on all fields but <['lightSaberColor']>", message);
  }

  @Test
  public void should_create_error_message_with_single_field_difference() {
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "green"), newArrayList("lightSaberColor"), newArrayList((Object) "green"),
                                                 newArrayList("lightSaberColor"));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting value <'green'> in field <'lightSaberColor'> of <Yoda the Jedi>."
        + "\nComparison was performed on all fields but <['lightSaberColor']>", message);
  }

  @Test
  public void should_create_error_message_with_all_fields_differences_without_ignored_fields() {
    List<String> ignoredFields = newArrayList();
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "green"), newArrayList("name", "lightSaberColor"),
                                                 newArrayList((Object) "Yoda", "green"), ignoredFields);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting values:\n  <['Yoda', 'green']>\nin fields:\n  <['name', 'lightSaberColor']>\n"
        + "of <Yoda the Jedi>.\n" + "Comparison was performed on all fields", message);
  }

  @Test
  public void should_create_error_message_with_single_field_difference_without_ignored_fields() {
    List<String> ignoredFields = newArrayList();
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "green"), newArrayList("lightSaberColor"), newArrayList((Object) "green"),
                                                 ignoredFields);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting value <'green'> in field <'lightSaberColor'> of <Yoda the Jedi>."
        + "\nComparison was performed on all fields", message);
  }

}
