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

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldSatisfy.UnsatisfiedRequirementError;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.util.Lists.newArrayList;

public class ElementsShouldSatisfy_create_Test {
  @Test
  public void should_create_error_message_all() {
    List<UnsatisfiedRequirementError> elementsNotSatisfyingRestrictions = newArrayList(new UnsatisfiedRequirementError("Leia",
                                                                                                                       "Leia violates some requirement."),
                                                                                       new UnsatisfiedRequirementError("Luke",
                                                                                                                       "Luke violates some requirement."));

    ErrorMessageFactory factory = elementsShouldSatisfy(newArrayList("Leia", "Luke", "Yoda"),
                                                        elementsNotSatisfyingRestrictions);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting all elements of:%n" +
                                         "  <[\"Leia\", \"Luke\", \"Yoda\"]>%n" +
                                         "to satisfy given requirements, but these elements did not:%n%n" +
                                         "  <Leia> Leia violates some requirement.%n%n" +
                                         "  <Luke> Luke violates some requirement."));
  }

  @Test
  public void should_create_error_message_any() {
    ErrorMessageFactory factory = elementsShouldSatisfyAny(newArrayList("Luke", "Yoda"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting any element of:%n" +
                                         "  <[\"Luke\", \"Yoda\"]>%n" +
                                         "to satisfy the given assertions requirements but none did."));
  }
}
