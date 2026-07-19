/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Strings.escapePercent;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains recursively a given set of values failed.
 *
 * @author David Haccoun
 * @author Joel Costigliola
 */
public class ShouldContainRecursively extends BasicErrorMessageFactory {

  /**
   * Creates an error for a file directory tree missing a matching entry.
   *
   * @param actual the actual directory
   * @param directoryContent the recursive directory content
   * @param filterDescription the filter description
   * @return the error message factory
   */
  public static ErrorMessageFactory directoryShouldContainRecursively(File actual, List<File> directoryContent,
                                                                      String filterDescription) {
    return new ShouldContainRecursively(actual, directoryContent.stream().map(File::toString).collect(toList()),
                                        filterDescription);
  }

  /**
   * Creates an error for a path directory tree missing a matching entry.
   *
   * @param actual the actual directory
   * @param directoryContent the recursive directory content
   * @param filterDescription the filter description
   * @return the error message factory
   */
  public static ErrorMessageFactory directoryShouldContainRecursively(Path actual, List<Path> directoryContent,
                                                                      String filterDescription) {
    return new ShouldContainRecursively(actual, directoryContent.stream().map(Path::toString).collect(toList()),
                                        filterDescription);
  }

  private ShouldContainRecursively(Object actual, List<String> directoryContent, String filterDescription) {
    super("%nExpecting directory or any of its subdirectories (recursively):%n" +
          "  %s%n" +
          "to contain at least one file matching %s but there was none.%n" +
          "The directory content was:%n  " + escapePercent("[" + String.join(", ", directoryContent) + "]"),
          actual, filterDescription);
  }

}
