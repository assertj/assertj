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
package org.assertj.core.api.file;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.AbstractFileAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_usingCharset_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_charset_is_null() {
    // GIVEN
    File actual = tempDir.toFile();
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).usingCharset((Charset) null));
    // THEN
    then(npe).hasMessage("The charset should not be null");
  }

  @Test
  void should_return_this() {
    // GIVEN
    AbstractFileAssert<?> fileAssert = assertThat(tempDir.toFile());
    // WHEN
    AbstractFileAssert<?> result = fileAssert.usingCharset(ISO_8859_1);
    // THEN
    then(result).isSameAs(fileAssert);
  }

  @Test
  void should_read_actual_content_with_given_charset() throws IOException {
    // GIVEN
    File actual = Files.writeString(tempDir.resolve("actual.txt"), "é à", ISO_8859_1).toFile();
    // WHEN/THEN
    then(actual).usingCharset(ISO_8859_1).hasContent("é à");
  }

  @Test
  void should_read_actual_content_with_default_charset_if_not_set() throws IOException {
    // GIVEN
    File actual = Files.writeString(tempDir.resolve("actual.txt"), "é à", Charset.defaultCharset()).toFile();
    // WHEN/THEN
    then(actual).hasContent("é à");
  }

}
