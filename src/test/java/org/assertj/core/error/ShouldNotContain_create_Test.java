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
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldNotContain#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldNotContain_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldNotContain(list("Yoda"), list("Luke", "Yoda"),
                                                   newLinkedHashSet("Yoda"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting%n" +
                                         " <[\"Yoda\"]>%n" +
                                         "not to contain%n" +
                                         " <[\"Luke\", \"Yoda\"]>%n" +
                                         "but found%n" +
                                         " <[\"Yoda\"]>%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldNotContain(list("Yoda"),
                                                   list("Luke", "Yoda"),
                                                   newLinkedHashSet("Yoda"),
                                                   new ComparatorBasedComparisonStrategy(
                                                                                         CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting%n" +
                                         " <[\"Yoda\"]>%n" +
                                         "not to contain%n" +
                                         " <[\"Luke\", \"Yoda\"]>%n" +
                                         "but found%n <[\"Yoda\"]>%n" +
                                         "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  public void should_create_error_message_for_file_directory() {
    // GIVEN
    File directory = mock(File.class);
    given(directory.getAbsolutePath()).willReturn("root");
    ErrorMessageFactory factory = directoryShouldNotContain(directory, list("foo.txt", "bar.txt"), "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting directory:%n" +
                                         "  <root>%n" +
                                         "not to contain any files matching glob:**.java but found some:%n" +
                                         "  [foo.txt, bar.txt]"));
  }

  @Test
  public void should_create_error_message_for_path_directory() {
    // GIVEN
    Path directory = mock(Path.class);
    given(directory.toString()).willReturn("root");
    ErrorMessageFactory factory = directoryShouldNotContain(directory, list("foo.txt", "bar.txt"), "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting directory:%n" +
                                         "  <root>%n" +
                                         "not to contain any files matching glob:**.java but found some:%n" +
                                         "  [foo.txt, bar.txt]"));
  }

}
