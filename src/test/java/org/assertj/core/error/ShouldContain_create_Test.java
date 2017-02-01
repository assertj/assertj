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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ShouldContain#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContain_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldContain(newArrayList("Yoda"), newArrayList("Luke", "Yoda"), newLinkedHashSet("Luke"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <[\"Yoda\"]>%n" +
                                         "to contain:%n" +
                                         " <[\"Luke\", \"Yoda\"]>%n" +
                                         "but could not find:%n" +
                                         " <[\"Luke\"]>%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    factory = shouldContain(newArrayList("Yoda"), newArrayList("Luke", "Yoda"), newLinkedHashSet("Luke"),
                            new ComparatorBasedComparisonStrategy(
                                                                  CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <[\"Yoda\"]>%n" +
                                         "to contain:%n" +
                                         " <[\"Luke\", \"Yoda\"]>%n" +
                                         "but could not find:%n" +
                                         " <[\"Luke\"]>%n" +
                                         "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

  @Test
  public void should_create_error_message_differentiating_long_from_integer() {
    factory = shouldContain(5L, 5, 5);
    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <5L>%n" +
                                         "to contain:%n" +
                                         " <5>%n" +
                                         "but could not find:%n" +
                                         " <5>%n" +
                                         ""));
  }

  @Test
  public void should_create_error_message_differentiating_long_from_integer_in_arrays() {
    factory = shouldContain(newArrayList(5L, 7L), newArrayList(5, 7), newLinkedHashSet(5, 7));
    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <[5L, 7L]>%n" +
                                         "to contain:%n" +
                                         " <[5, 7]>%n" +
                                         "but could not find:%n" +
                                         " <[5, 7]>%n" +
                                         ""));
  }

  @Test
  public void should_create_error_message_differentiating_double_from_float() {
    factory = shouldContain(newArrayList(5d, 7d), newArrayList(5f, 7f), newLinkedHashSet(5f, 7f));
    String message = factory.create(new TextDescription("Test"));
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <[5.0, 7.0]>%n" +
                                         "to contain:%n" +
                                         " <[5.0f, 7.0f]>%n" +
                                         "but could not find:%n" +
                                         " <[5.0f, 7.0f]>%n" +
                                         ""));
  }

}
