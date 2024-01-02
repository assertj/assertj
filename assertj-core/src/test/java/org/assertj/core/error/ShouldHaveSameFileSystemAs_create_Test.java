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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameFileSystemAs.shouldHaveSameFileSystemAs;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ShouldHaveSameFileSystemAs}.
 *
 * @author Ashley Scopes
 */
class ShouldHaveSameFileSystemAs_create_Test {

  @Test
  void should_create_error_message_for_expected_path() {
    // GIVEN
    Path actual = Paths.get("/foo/bar");
    Path expected = Paths.get("/baz/bork");
    // WHEN
    String message = shouldHaveSameFileSystemAs(actual, expected).create();
    // THEN
    then(message).isEqualTo(format("%nExpecting path:%n" +
                                   "  %sfoo%sbar%n" +
                                   "to have the same file system as path:%n" +
                                   "  %sbaz%sbork",
                                   File.separator, File.separator, File.separator, File.separator));
  }

  @Test
  void should_create_error_message_for_expected_path_with_test_description() {
    // GIVEN
    Path actual = Paths.get("/foo/bar");
    Path expected = Paths.get("/baz/bork");
    // WHEN
    String message = shouldHaveSameFileSystemAs(actual, expected).create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting path:%n" +
                                   "  %sfoo%sbar%n" +
                                   "to have the same file system as path:%n" +
                                   "  %sbaz%sbork",
                                   File.separator, File.separator, File.separator, File.separator));
  }
}
