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

import static org.assertj.core.util.Strings.escapePercent;

import java.io.File;
import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains recursively a given set of values failed.
 *
 * @author David Haccoun
 * @author Joel Costigliola
 */
public class ShouldContainRecursively extends BasicErrorMessageFactory {

  public static ErrorMessageFactory directoryShouldContainRecursively(File actual, List<String> directoryContent,
                                                                      String filterDescription) {
    return new ShouldContainRecursively(actual, directoryContent, filterDescription);
  }

  private ShouldContainRecursively(Object actual, List<String> directoryContent, String filterDescription) {
    // not passing directoryContent and filterDescription as parameter to avoid AssertJ default String formatting
    super("%nExpecting directory or any of its subdirectories(recursively):%n" +
          "  <%s>%n" +
          "to contain at least one file matching " + escapePercent(filterDescription) + " but there was none.%n" +
          "The directory content was:%n  " + escapePercent(directoryContent.toString()),
          actual);
  }

}
