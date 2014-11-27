/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies that two files/inputStreams/readers have equal
 * content failed.
 *
 * @author Yvonne Wang
 * @author Matthieu Baechler
 * @author Joel Costigliola
 * @author Bartosz Bierkowski
 */
public class ShouldHaveEqualContent extends AbstractShouldHaveTextContent {

  /**
   * Creates a new <code>{@link ShouldHaveEqualContent}</code>.
   * 
   * @param actual the actual reader in the failed assertion.
   * @param expected the expected reader in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveEqualContent(Reader actual, Reader expected, List<String> diffs) {
    return new ShouldHaveEqualContent(actual, expected, diffsAsString(diffs));
  }

  /**
   * Creates a new <code>{@link ShouldHaveEqualContent}</code>.
   * 
   * @param actual the actual file in the failed assertion.
   * @param expected the expected file in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveEqualContent(File actual, File expected, List<String> diffs) {
    return new ShouldHaveEqualContent(actual, expected, diffsAsString(diffs));
  }

  /**
   * Creates a new <code>{@link ShouldHaveEqualContent}</code>.
   * 
   * @param actual the actual InputStream in the failed assertion.
   * @param expected the expected Stream in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveEqualContent(InputStream actual, InputStream expected, List<String> diffs) {
    return new ShouldHaveEqualContent(actual, expected, diffsAsString(diffs));
  }

  private ShouldHaveEqualContent(File actual, File expected, String diffs) {
    super("\nFile:\n <%s>\nand file:\n <%s>\ndo not have equal content:", actual, expected);
    this.diffs = diffs;
  }

  private ShouldHaveEqualContent(InputStream actual, InputStream expected, String diffs) {
    super("\nInputStreams do not have equal content:", actual, expected);
    this.diffs = diffs;
  }

  private ShouldHaveEqualContent(Reader actual, Reader expected, String diffs) {
    super("\nReaders do not have equal content:", actual, expected);
    this.diffs = diffs;
  }
}
