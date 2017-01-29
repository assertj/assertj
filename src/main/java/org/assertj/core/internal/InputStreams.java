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
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;


/**
 * Reusable assertions for <code>{@link InputStream}</code>s.
 * 
 * @author Matthieu Baechler
 */
public class InputStreams {

  private static final InputStreams INSTANCE = new InputStreams();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static InputStreams instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Diff diff = new Diff();
  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  InputStreams() {}

  /**
   * Asserts that the given InputStreams have same content.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" InputStream.
   * @param expected the "expected" InputStream.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the given InputStreams do not have same content.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public void assertSameContentAs(AssertionInfo info, InputStream actual, InputStream expected) {
    checkNotNull(expected, "The InputStream to compare to should not be null");
    assertNotNull(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, expected);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (IOException e) {
      String msg = format("Unable to compare contents of InputStreams:%n  <%s>%nand:%n  <%s>", actual, expected);
      throw new InputStreamsException(msg, e);
    }
  }

  private static void assertNotNull(AssertionInfo info, InputStream stream) {
    Objects.instance().assertNotNull(info, stream);
  }
}
