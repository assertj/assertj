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
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ElementsShouldSatisfy.UnsatisfiedRequirement;
import org.junit.jupiter.api.Test;

public class ElementsShouldSatisfy_create_Test {

  @Test
  public void should_create_error_message_all() {
    // GIVEN
    List<UnsatisfiedRequirement> unsatisfiedRequirements = list(new UnsatisfiedRequirement("Leia", "Leia mistake."),
                                                                new UnsatisfiedRequirement("Luke", "Luke mistake."));
    ErrorMessageFactory factory = elementsShouldSatisfy(list("Leia", "Luke", "Yoda"), unsatisfiedRequirements);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting all elements of:%n" +
                                         "  <[\"Leia\", \"Luke\", \"Yoda\"]>%n" +
                                         "to satisfy given requirements, but these elements did not:%n%n" +
                                         "  <Leia> Leia mistake.%n%n" +
                                         "  <Luke> Luke mistake."));
  }

  @Test
  public void should_create_error_message_all_and_escape_percent_correctly() {
    // GIVEN
    List<UnsatisfiedRequirement> unsatisfiedRequirements = list(new UnsatisfiedRequirement("Leia%s", "Leia mistake."),
                                                                new UnsatisfiedRequirement("Luke", "Luke mistake."));
    ErrorMessageFactory factory = elementsShouldSatisfy(list("Leia%s", "Luke", "Yoda"), unsatisfiedRequirements);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting all elements of:%n" +
                                         "  <[\"Leia%%s\", \"Luke\", \"Yoda\"]>%n" +
                                         "to satisfy given requirements, but these elements did not:%n%n" +
                                         "  <Leia%%s> Leia mistake.%n%n" +
                                         "  <Luke> Luke mistake."));
  }

  @Test
  public void should_create_error_message_any() {
    // GIVEN
    ErrorMessageFactory factory = elementsShouldSatisfyAny(list("Luke", "Yoda"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting any element of:%n" +
                                         "  <[\"Luke\", \"Yoda\"]>%n" +
                                         "to satisfy the given assertions requirements but none did."));
  }

  @Test
  public void should_create_error_message_any_and_escape_percent_correctly() {
    // GIVEN
    ErrorMessageFactory factory = elementsShouldSatisfyAny(list("Lu%dke", "Yoda"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting any element of:%n" +
                                         "  <[\"Lu%%dke\", \"Yoda\"]>%n" +
                                         "to satisfy the given assertions requirements but none did."));
  }
}
