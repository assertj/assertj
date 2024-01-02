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
import static org.assertj.core.error.ShouldStartWithIgnoringCase.shouldStartWithIgnoringCase;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.OtherStringTestComparator;
import org.junit.jupiter.api.Test;

class ShouldStartWithIgnoringCase_create_Test {

  private ErrorMessageFactory factory;

  @Test
  void should_create_error_message() {
    // GIVEN
    factory = shouldStartWithIgnoringCase("Gandalf% the grey", "grey%", StandardComparisonStrategy.instance());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Gandalf%% the grey\"%n" +
                                   "to start with (ignoring case):%n" +
                                   "  \"grey%%\"%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    factory = shouldStartWithIgnoringCase("Gandalf the grey", "grey",
                                          new ComparatorBasedComparisonStrategy(new OtherStringTestComparator()));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Gandalf the grey\"%n" +
                                   "to start with (ignoring case):" +
                                   "%n  \"grey\"%n" +
                                   "when comparing values using other String comparator"));
  }
}
