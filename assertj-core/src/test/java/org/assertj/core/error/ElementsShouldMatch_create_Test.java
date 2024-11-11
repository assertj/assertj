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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

class ElementsShouldMatch_create_Test {

  @Test
  void should_create_error_message_with_one_non_matching_element() {
    // GIVEN
    ErrorMessageFactory factory = elementsShouldMatch(list("Luke", "Yoda"), "Yoda", PredicateDescription.GIVEN);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting all elements of:%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "to match given predicate but this element did not:%n" +
                                   "  \"Yoda\""));
  }

  @Test
  void should_create_error_message_with_multiple_non_matching_elements() {
    // GIVEN
    ErrorMessageFactory factory = elementsShouldMatch(list("Luke", "Yoda"),
                                                      list("Luke", "Yoda"),
                                                      PredicateDescription.GIVEN);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting all elements of:%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "to match given predicate but these elements did not:%n" +
                                   "  [\"Luke\", \"Yoda\"]"));
  }

  @Test
  void should_create_error_message_with_custom_description() {
    // GIVEN
    ErrorMessageFactory factory = elementsShouldMatch(list("Luke", "Yoda"), "Yoda",
                                                      new PredicateDescription("custom"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting all elements of:%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "to match 'custom' predicate but this element did not:%n" +
                                   "  \"Yoda\""));
  }

}
