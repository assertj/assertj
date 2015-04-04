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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;


import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies that two files/inputStreams have same content failed.
 * 
 * @author Yvonne Wang
 * @author Matthieu Baechler
 * @author Joel Costigliola
 */
public class ShouldHaveSameContent extends AbstractShouldHaveTextContent {

  /**
   * Creates a new <code>{@link ShouldHaveSameContent}</code>.
   * @param actual the actual file in the failed assertion.
   * @param expected the expected file in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSameContent(File actual, File expected, List<String> diffs) {
    return new ShouldHaveSameContent(actual, expected, diffsAsString(diffs));
  }

  /**
   * Creates a new <code>{@link ShouldHaveSameContent}</code>.
   * @param actual the actual InputStream in the failed assertion.
   * @param expected the expected Stream in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSameContent(InputStream actual, InputStream expected, List<String> diffs) {
    return new ShouldHaveSameContent(actual, expected, diffsAsString(diffs));
  }

  private ShouldHaveSameContent(File actual, File expected, String diffs) {
    super("%nFile:%n  <%s>%nand file:%n  <%s>%ndo not have same content:", actual, expected);
    this.diffs = diffs;
  }

  private ShouldHaveSameContent(InputStream actual, InputStream expected, String diffs) {
    super("%nInputStreams do not have same content:", actual, expected);
    this.diffs = diffs;
  }
}
