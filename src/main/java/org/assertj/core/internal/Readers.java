/*
 * Created on Jan 26, 2011
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
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldHaveEqualContent.shouldHaveEqualContent;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link java.io.Reader}</code>s.
 *
 * @author Matthieu Baechler
 * @author Bartosz Bierkowski
 */
public class Readers {

  private static final Readers INSTANCE = new Readers();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Readers instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Diff diff = new Diff();
  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Readers() {
  }

  /**
   * Asserts that the given Readers have equal content.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" Reader.
   * @param expected the "expected" Reader.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the given Readers do not have equal content.
   * @throws org.assertj.core.internal.ReadersException if an I/O error occurs.
   */
  public void assertEqualContent(AssertionInfo info, Reader actual, Reader expected) {
    if (expected == null)
      throw new NullPointerException("The Reader to compare to should not be null");
    assertNotNull(info, actual);
    try {
      List<String> diffs = diff.diff(actual, expected);
      if (diffs.isEmpty())
        return;
      throw failures.failure(info, shouldHaveEqualContent(actual, expected, diffs));
    } catch (IOException e) {
      String msg = format("Unable to compare contents of Readers:\n  <%s>\nand:\n  <%s>", actual, expected);
      throw new ReadersException(msg, e);
    }
  }

  private static void assertNotNull(AssertionInfo info, Reader reader) {
    Objects.instance().assertNotNull(info, reader);
  }
}
