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
package org.fest.assertions.api;

import java.io.File;

import org.fest.assertions.internal.Files;
import org.fest.util.*;

/**
 * Assertion methods for <code>{@link File}</code>s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(File)}</code>.
 * </p>
 *
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class FileAssert extends AbstractAssert<FileAssert, File> {

  @VisibleForTesting Files files = Files.instance();

  protected FileAssert(File actual) {
    super(actual, FileAssert.class);
  }

  /**
   * Verifies that the actual {@code File} exists, regardless it's a file or directory.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} does not exist.
   */
  public FileAssert exists() {
    files.assertExists(info, actual);
    return this;
  }

  /**
   * Verifies that the actual {@code File} is an existing file.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   */
  public FileAssert isFile() {
    files.assertIsFile(info, actual);
    return this;
  }

  /**
   * Verifies that the content of the actual {@code File} is equal to the content of the given one.
   * @param expected the given {@code File} to compare the actual {@code File} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code File} is {@code null}.
   * @throws IllegalArgumentException if the given {@code File} is not an existing file.
   * @throws AssertionError if the actual {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code File} is not an existing file.
   * @throws FilesException if an I/O error occurs.
   * @throws AssertionError if the content of the actual {@code File} is not equal to the content of the given one.
   */
  public FileAssert hasContentEqualTo(File expected) {
    files.assertEqualContent(info, actual, expected);
    return this;
  }
}
