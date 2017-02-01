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

import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldContainsOnlyOnce#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author William Delanoue
 */
public class ShouldContainsOnlyOnce_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldContainsOnlyOnce(newArrayList("Yoda", "Han", "Han"), newArrayList("Luke", "Yoda"),
                                     newLinkedHashSet("Luke"), newLinkedHashSet("Han"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n" +
            " <[\"Yoda\", \"Han\", \"Han\"]>%n" +
            "to contain only once:%n" +
            " <[\"Luke\", \"Yoda\"]>%n" +
            "but some elements were not found:%n" +
            " <[\"Luke\"]>%n" +
            "and others were found more than once:%n" +
            " <[\"Han\"]>%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainsOnlyOnce(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"),
                                                         newLinkedHashSet("Luke"), newLinkedHashSet("Han"), new ComparatorBasedComparisonStrategy(
                                                                                                                                                  CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %n" +
            "Expecting:%n" +
            " <[\"Yoda\", \"Han\"]>%n" +
            "to contain only once:%n" +
            " <[\"Luke\", \"Yoda\"]>%n" +
            "but some elements were not found:%n" +
            " <[\"Luke\"]>%n" +
            "and others were found more than once:%n" +
            " <[\"Han\"]>%n" +
            "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_create_error_message_without_not_found_elements() {
    factory = shouldContainsOnlyOnce(newArrayList("Yoda", "Han", "Han"), newArrayList("Yoda"), newLinkedHashSet(),
                                     newLinkedHashSet("Han"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n" +
            " <[\"Yoda\", \"Han\", \"Han\"]>%n" +
            "to contain only once:%n" +
            " <[\"Yoda\"]>%n" +
            "but some elements were found more than once:%n" +
            " <[\"Han\"]>%n"));
  }

  @Test
  public void should_create_error_message_without_elements_found_many_times() {
    factory = shouldContainsOnlyOnce(newArrayList("Yoda", "Han"), newArrayList("Luke"), newLinkedHashSet("Luke"),
                                     newLinkedHashSet());
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n" +
            " <[\"Yoda\", \"Han\"]>%n" +
            "to contain only once:%n" +
            " <[\"Luke\"]>%n" +
            "but some elements were not found:%n" +
            " <[\"Luke\"]>%n"));
  }
  
}
