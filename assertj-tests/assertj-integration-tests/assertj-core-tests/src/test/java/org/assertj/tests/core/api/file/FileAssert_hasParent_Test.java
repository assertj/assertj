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
package org.assertj.tests.core.api.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_hasParent_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    File expected = tempDir.toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasParent(expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    File actual = tempDir.resolve("child").toFile();
    File expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasParent(expected));
    // THEN
    then(npe).hasMessage("The expected parent file should not be null.");
  }

  @Test
  void should_fail_if_actual_has_no_parent() {
    // GIVEN
    File actual = new File("without-parent");
    File expected = new File("some");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasParent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveParent(actual, expected).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_the_expected_parent() {
    // GIVEN
    File actual = tempDir.resolve("some/child").toFile();
    File expected = tempDir.resolve("expected-parent").toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasParent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveParent(actual, expected).create());
  }

  @Test
  void should_pass_if_actual_has_the_expected_parent() {
    // GIVEN
    File actual = tempDir.resolve("some/child").toFile();
    File expected = tempDir.resolve("some").toFile();
    // WHEN/THEN
    then(actual).hasParent(expected);
  }

  @Test
  void should_pass_if_actual_has_the_expected_parent_when_both_are_relative() {
    // GIVEN
    File actual = new File("./some/child");
    File expected = new File("./some");
    // WHEN/THEN
    then(actual).hasParent(expected);
  }

  @Test
  void should_pass_if_actual_has_the_expected_parent_when_actual_form_is_absolute() {
    // GIVEN
    File actual = new File("./some/child").getAbsoluteFile();
    File expected = new File("./some");
    // WHEN/THEN
    then(actual).hasParent(expected);
  }

  @Test
  void should_pass_if_actual_has_the_expected_parent_when_actual_form_is_canonical() throws IOException {
    // GIVEN
    File actual = new File("./some/child").getCanonicalFile();
    File expected = new File("./some");
    // WHEN/THEN
    then(actual).hasParent(expected);
  }

  @Test
  void should_throw_UncheckedIOException_if_canonical_form_of_actual_parent_cannot_be_computed() {
    // GIVEN
    // a path containing a NUL character is invalid, so File#getCanonicalFile() throws an IOException
    File actual = new File(new File(tempDir.toFile(), "invalid\0parent"), "child");
    File expected = tempDir.toFile();
    // WHEN
    var uncheckedIOException = catchThrowableOfType(UncheckedIOException.class,
                                                    () -> assertThat(actual).hasParent(expected));
    // THEN
    then(uncheckedIOException).hasMessageStartingWith("Unable to get canonical form of");
  }

  @Test
  void should_throw_UncheckedIOException_if_canonical_form_of_expected_cannot_be_computed() {
    // GIVEN
    File actual = tempDir.resolve("child").toFile();
    // a path containing a NUL character is invalid, so File#getCanonicalFile() throws an IOException
    File expected = new File(tempDir.toFile(), "invalid\0parent");
    // WHEN
    var uncheckedIOException = catchThrowableOfType(UncheckedIOException.class,
                                                    () -> assertThat(actual).hasParent(expected));
    // THEN
    then(uncheckedIOException).hasMessageStartingWith("Unable to get canonical form of");
  }

}
