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

import org.assertj.core.description.*;
import org.assertj.core.internal.*;
import org.assertj.core.presentation.*;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.*;

import java.util.*;

import static java.lang.String.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.*;
import static org.assertj.core.util.Lists.*;
import static org.assertj.core.util.Sets.*;

/**
 * Tests for
 * <code>{@link ShouldContainExactlyInAnyOrder#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContainExactlyInAnyOrder_create_Test {

  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainExactlyInAnyOrder(newArrayList("Yoda", "Han"),
                                                    newArrayList("Luke", "Yoda"),
                                                    newLinkedHashSet("Luke"),
                                                    newLinkedHashSet("Han"),
                                                    CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    assertThat(message).isEqualTo(format("[Test] %n"
                                         + "Expecting:%n"
                                         + "  <[\"Yoda\", \"Han\"]>%n"
                                         + "to contain exactly in any order:%n"
                                         + "  <[\"Luke\", \"Yoda\"]>%n"
                                         + "elements not found:%n"
                                         + "  <[\"Luke\"]>%n"
                                         + "and elements not expected:%n"
                                         + "  <[\"Han\"]>%n"
                                         + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_not_display_unexpected_elements_when_there_are_none_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainExactlyInAnyOrder(newArrayList("Yoda"),
                                                    newArrayList("Luke", "Yoda"),
                                                    newLinkedHashSet("Luke"),
                                                    Collections.emptySet(),
                                                    CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    assertThat(message).isEqualTo(format("[Test] %n"
                                         + "Expecting:%n"
                                         + "  <[\"Yoda\"]>%n"
                                         + "to contain exactly in any order:%n"
                                         + "  <[\"Luke\", \"Yoda\"]>%n"
                                         + "but could not find the following elements:%n"
                                         + "  <[\"Luke\"]>%n"
                                         + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_not_display_elements_not_found_when_there_are_none_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainExactlyInAnyOrder(newArrayList("Yoda", "Leia"),
                                                    newArrayList("Yoda"),
                                                    Collections.emptySet(),
                                                    newLinkedHashSet("Leia"),
                                                    CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    assertThat(message).isEqualTo(format("[Test] %n"
                                         + "Expecting:%n"
                                         + "  <[\"Yoda\", \"Leia\"]>%n"
                                         + "to contain exactly in any order:%n"
                                         + "  <[\"Yoda\"]>%n"
                                         + "but the following elements were unexpected:%n"
                                         + "  <[\"Leia\"]>%n"
                                         + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }
}
