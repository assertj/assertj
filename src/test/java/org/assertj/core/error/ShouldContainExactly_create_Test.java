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
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldHaveSameSize;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Collections;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Test;

public class ShouldContainExactly_create_Test {

  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON_STRATEGY =
      new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldContainExactly(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"),
                                                       newLinkedHashSet("Luke"), newLinkedHashSet("Han"));

    String message = factory.create(new TextDescription("Test"));

    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Expecting:%n"
                                                + "  <[\"Yoda\", \"Han\"]>%n"
                                                + "to contain exactly (and in same order):%n"
                                                + "  <[\"Luke\", \"Yoda\"]>%n"
                                                + "but some elements were not found:%n"
                                                + "  <[\"Luke\"]>%n"
                                                + "and others were not "
                                                + "expected:%n"
                                                + "  <[\"Han\"]>%n"));
  }

  @Test
  public void should_create_error_message_when_sizes_differ() {
    ErrorMessageFactory factory = shouldHaveSameSize(newArrayList("Yoda", "Han"), newArrayList("Yoda"), 2, 1,
                                                     StandardComparisonStrategy.instance());

    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Actual and expected should have same size but actual size was:%n"
                                                + "  <2>%n"
                                                + "while expected size was:%n"
                                                + "  <1>%n"
                                                + "Actual was:%n"
                                                + "  <[\"Yoda\", \"Han\"]>%n"
                                                + "Expected was:%n"
                                                + "  <[\"Yoda\"]>%n"));
  }

  @Test
  public void should_create_error_message_when_sizes_differ_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldHaveSameSize(newArrayList("Yoda", "Han"), newArrayList("Yoda"),
                                                     2, 1, CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Actual and expected should have same size but actual size was:%n"
                                                + "  <2>%n"
                                                + "while expected size was:%n"
                                                + "  <1>%n"
                                                + "Actual was:%n"
                                                + "  <[\"Yoda\", \"Han\"]>%n"
                                                + "Expected was:%n"
                                                + "  <[\"Yoda\"]>%n"
                                                + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainExactly(newArrayList("Yoda", "Han"),
                                                       newArrayList("Luke", "Yoda"),
                                                       newLinkedHashSet("Luke"),
                                                       newLinkedHashSet("Han"),
                                                       CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"));

    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Expecting:%n"
                                                + "  <[\"Yoda\", \"Han\"]>%n"
                                                + "to contain exactly (and in same order):%n"
                                                + "  <[\"Luke\", \"Yoda\"]>%n"
                                                + "but some elements were not found:%n"
                                                + "  <[\"Luke\"]>%n"
                                                + "and others were not expected:%n"
                                                + "  <[\"Han\"]>%n"
                                                + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_not_display_unexpected_elements_when_there_are_none() {
    ErrorMessageFactory factory = shouldContainExactly(newArrayList("Yoda"), newArrayList("Luke", "Yoda"),
                                                       newLinkedHashSet("Luke"), Collections.emptySet());

    String message = factory.create(new TextDescription("Test"));

    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Expecting:%n"
                                                + "  <[\"Yoda\"]>%n"
                                                + "to contain exactly (and in same order):%n"
                                                + "  <[\"Luke\", \"Yoda\"]>%n"
                                                + "but could not find the following elements:%n"
                                                + "  <[\"Luke\"]>%n"));
  }

  @Test
  public void should_not_display_unexpected_elements_when_there_are_none_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainExactly(newArrayList("Yoda"),
                                                       newArrayList("Luke", "Yoda"),
                                                       newLinkedHashSet("Luke"),
                                                       Collections.emptySet(),
                                                       CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"));

    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Expecting:%n"
                                                + "  <[\"Yoda\"]>%n"
                                                + "to contain exactly (and in same order):%n"
                                                + "  <[\"Luke\", \"Yoda\"]>%n"
                                                + "but could not find the following elements:%n"
                                                + "  <[\"Luke\"]>%n"
                                                + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_create_error_message_when_only_elements_order_differs() {
    ErrorMessageFactory factory = elementsDifferAtIndex("Luke", "Han", 1);

    String message = factory.create(new TextDescription("Test"));

    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Actual and expected have the same elements but not in the same order, at index 1 actual element was:%n"
                                                + "  <\"Luke\">%n"
                                                + "whereas expected element was:%n"
                                                + "  <\"Han\">%n"));
  }

  @Test
  public void should_create_error_message_when_only_elements_order_differs_according_to_custom_comparison_strategy() {
    ErrorMessageFactory factory = elementsDifferAtIndex("Luke", "Han", 1, CASE_INSENSITIVE_COMPARISON_STRATEGY);

    String message = factory.create(new TextDescription("Test"));

    assertThat(message).isEqualTo(String.format("[Test] %n"
                                                + "Actual and expected have the same elements but not in the same order, at index 1 actual element was:%n"
                                                + "  <\"Luke\">%nwhereas expected element was:%n"
                                                + "  <\"Han\">%n"
                                                + "when comparing values using 'CaseInsensitiveStringComparator'"));
  }
}
