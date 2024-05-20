/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.TempFileUtil.createTempFileWithContent;
import static org.mockito.Mockito.verify;

import java.io.File;
import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FileAssert#hasSameBinaryContentAs(File)}</code>.
 *
 * @author Nikolaos Georgiou
 */
class FileAssert_hasSameBinaryContentAs_Test extends FileAssertBaseTest {

  private static File expected;

  @BeforeAll
  static void beforeOnce() {
    expected = new File("xyz");
  }

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasSameBinaryContentAs(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertSameBinaryContentAs(getInfo(assertions), getActual(assertions), expected);
  }

  @Test
  void should_pass_on_equal_files() throws Exception {
    // GIVEN
    File actual = createTempFileWithContent("assertJ");
    File expected = createTempFileWithContent("assertJ");
    // WHEN/THEN
    then(actual).hasSameBinaryContentAs(expected);
  }

  @Test
  void should_fail_on_different_files() throws Exception {
    // GIVEN
    File actual = createTempFileWithContent("assertJ");
    File expected = createTempFileWithContent("assertJ++");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessageContaining("does not have expected binary content at offset");
  }
}
