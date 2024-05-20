/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
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
class ShouldNotContain_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContain(list("Yoda"), list("Luke", "Yoda"),
                                                   newLinkedHashSet("Yoda"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  [\"Yoda\"]%n" +
                                   "not to contain%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "but found%n" +
                                   "  [\"Yoda\"]%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContain(list("Yoda"),
                                                   list("Luke", "Yoda"),
                                                   newLinkedHashSet("Yoda"),
                                                   new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  [\"Yoda\"]%n" +
                                   "not to contain%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "but found%n  [\"Yoda\"]%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_create_error_message_for_file_directory() {
    // GIVEN
    File directory = mock(File.class);
    given(directory.getAbsolutePath()).willReturn("root");
    List<File> matchingContent = list(new File("root", "foo.txt"), new File("root", "bar.txt"));
    ErrorMessageFactory factory = directoryShouldNotContain(directory, matchingContent, "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting directory:%n" +
                                   "  root%n" +
                                   "not to contain any files matching glob:**.java but found some:%n" +
                                   "  [foo.txt, bar.txt]"));
  }

  @Test
  void should_create_error_message_for_path_directory() {
    // GIVEN
    Path directory = Paths.get("root");
    List<Path> matchingContent = list(directory.resolve("foo.txt"), directory.resolve("bar.txt"));
    ErrorMessageFactory factory = directoryShouldNotContain(directory, matchingContent, "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting directory:%n" +
                                   "  root%n" +
                                   "not to contain any files matching glob:**.java but found some:%n" +
                                   "  [%s, %s]",
                                   directory.resolve("foo.txt"), directory.resolve("bar.txt")));
  }

}
