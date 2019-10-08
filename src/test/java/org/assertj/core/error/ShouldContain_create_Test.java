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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

  @BeforeEach
  public void setUp() {
    factory = shouldContain(list("Yoda"), list("Luke", "Yoda"), newLinkedHashSet("Luke"));
  }

  @Test
  public void should_create_error_message() {
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
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
    // GIVEN
    factory = shouldContain(list("Yoda"), list("Luke", "Yoda"), newLinkedHashSet("Luke"),
                            new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <[\"Yoda\"]>%n" +
                                         "to contain:%n" +
                                         " <[\"Luke\", \"Yoda\"]>%n" +
                                         "but could not find:%n" +
                                         " <[\"Luke\"]>%n" +
                                         "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  public void should_create_error_message_differentiating_long_from_integer() {
    // GIVEN
    factory = shouldContain(5L, 5, 5);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
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
    // GIVEN
    factory = shouldContain(list(5L, 7L), list(5, 7), newLinkedHashSet(5, 7));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
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
    // GIVEN
    factory = shouldContain(list(5d, 7d), list(5f, 7f), newLinkedHashSet(5f, 7f));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <[5.0, 7.0]>%n" +
                                         "to contain:%n" +
                                         " <[5.0f, 7.0f]>%n" +
                                         "but could not find:%n" +
                                         " <[5.0f, 7.0f]>%n" +
                                         ""));
  }

  @Test
  public void should_create_error_message_for_file_directory() {
    // GIVEN
    File directory = mock(File.class);
    given(directory.getAbsolutePath()).willReturn("root");
    factory = directoryShouldContain(directory, list("foo.txt", "bar.txt"), "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting directory:%n" +
                                         "  <root>%n" +
                                         "to contain at least one file matching glob:**.java but there was none.%n" +
                                         "The directory content was:%n" +
                                         "  [foo.txt, bar.txt]"));
  }

  @Test
  void should_create_error_message_for_file_directory_escaping_percent() {
    // GIVEN
    File directory = mock(File.class);
    given(directory.getAbsolutePath()).willReturn("root%dir");
    factory = directoryShouldContain(directory, list("foo%1.txt", "bar%2.txt"), "glob:**%Test.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting directory:%n" +
                                         "  <root%%dir>%n" +
                                         "to contain at least one file matching glob:**%%Test.java but there was none.%n" +
                                         "The directory content was:%n" +
                                         "  [foo%%1.txt, bar%%2.txt]"));
  }

  @Test
  public void should_create_error_message_for_path_directory() {
    // GIVEN
    Path directory = mock(Path.class);
    given(directory.toString()).willReturn("root");
    factory = directoryShouldContain(directory, list("foo.txt", "bar.txt"), "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting directory:%n" +
                                         "  <root>%n" +
                                         "to contain at least one file matching glob:**.java but there was none.%n" +
                                         "The directory content was:%n" +
                                         "  [foo.txt, bar.txt]"));
  }

}
