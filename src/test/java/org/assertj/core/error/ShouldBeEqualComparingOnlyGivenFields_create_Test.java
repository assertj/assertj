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
import static org.assertj.core.error.ShouldBeEqualByComparingOnlyGivenFields.shouldBeEqualComparingOnlyGivenFields;
import static org.assertj.core.util.Lists.*;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.Jedi;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeEqualToIgnoringFields#create(Description)}</code>.
 *
 * @author Nicolas François
 */
public class ShouldBeEqualComparingOnlyGivenFields_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message_with_all_fields_differences() {
    factory = shouldBeEqualComparingOnlyGivenFields(new Jedi("Luke", "blue"), newArrayList("name", "lightSaberColor"),
                                                    newArrayList((Object) "Luke", "blue"),
                                                    newArrayList((Object) "Yoda", "green"), newArrayList("name",
                                                                                                         "lightSaberColor"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %n" +
                                  "Expecting values:%n" +
                                  "  <[\"Yoda\", \"green\"]>%n" +
                                  "in fields:%n" +
                                  "  <[\"name\", \"lightSaberColor\"]>%n" +
                                  "but were:%n" +
                                  "  <[\"Luke\", \"blue\"]>%n" +
                                  "in <Luke the Jedi>.%n" +
                                  "Comparison was performed on fields:%n" +
                                  "  <[\"name\", \"lightSaberColor\"]>"));
  }

  @Test
  public void should_create_error_message_with_single_field_difference() {
    factory = shouldBeEqualComparingOnlyGivenFields(new Jedi("Yoda", "green"), newArrayList("lightSaberColor"),
                                                    newArrayList((Object) "green"), newArrayList((Object) "blue"),
                                                    newArrayList("lightSaberColor"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting value <\"blue\">" +
                                  " in field <\"lightSaberColor\">" +
                                  " but was <\"green\">" +
                                  " in <Yoda the Jedi>"));
  }

}
