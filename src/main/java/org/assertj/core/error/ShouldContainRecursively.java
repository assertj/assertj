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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

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

  public static ErrorMessageFactory directoryShouldContainRecursively(File actual, List<File> directoryContent,
                                                                      String filterDescription) {
    return new ShouldContainRecursively(actual, directoryContent, filterDescription);
  }

  public static ErrorMessageFactory directoryShouldContainRecursively(Path actual, List<Path> directoryContent,
                                                                      String filterDescription) {
    return new ShouldContainRecursively(actual, directoryContent, filterDescription);
  }

  private ShouldContainRecursively(Object actual, List<?> directoryContent, String filterDescription) {
    super("%nExpecting directory or any of its subdirectories (recursively):%n" +
          "   <%s>%n" +
          "to contain at least one file matching %s but there was none.%n" +
          "The directory content was:%n   %s",
          actual, filterDescription, directoryContent);
  }

}
