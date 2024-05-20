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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Paths.linesOf;

import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Paths#linesOf(Path, Charset)} and {@link Paths#linesOf(Path, String)}.
 * 
 * @author Stefan Bratanov
 * @author Mateusz Haligowski
 */
class Paths_linesOf_Test {

  private static final Path RESOURCES_DIRECTORY = java.nio.file.Paths.get("src", "test", "resources");

  private static final Path SAMPLE_UNIX_FILE = RESOURCES_DIRECTORY.resolve("utf8.txt");
  private static final Path SAMPLE_WIN_FILE = RESOURCES_DIRECTORY.resolve("utf8_win.txt");
  private static final Path SAMPLE_MAC_FILE = RESOURCES_DIRECTORY.resolve("utf8_mac.txt");

  private static final List<String> EXPECTED_CONTENT = newArrayList("A text file encoded in UTF-8, with diacritics:", "é à");
  public static final String UTF_8 = "UTF-8";

  @Test
  void should_throw_exception_when_charset_is_null() {
    Charset charset = null;
    assertThatNullPointerException().isThrownBy(() -> linesOf(SAMPLE_UNIX_FILE, charset));
  }

  @Test
  void should_throw_exception_if_charset_name_does_not_exist() {
    assertThatIllegalArgumentException().isThrownBy(() -> linesOf(java.nio.file.Paths.get("test"), "Klingon"));
  }

  @Test
  void should_throw_exception_if_path_not_found() {
    Path missingFile = java.nio.file.Paths.get("missing.txt");
    assertThat(missingFile).doesNotExist();

    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> linesOf(missingFile,
                                                                                   Charset.defaultCharset()));
  }

  @Test
  void should_pass_if_unix_path_is_split_into_lines() {
    assertThat(linesOf(SAMPLE_UNIX_FILE, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  void should_pass_if_unix_path_is_split_into_lines_using_charset() {
    assertThat(linesOf(SAMPLE_UNIX_FILE, UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  void should_pass_if_windows_path_is_split_into_lines() {
    assertThat(linesOf(SAMPLE_WIN_FILE, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  void should_pass_if_windows_path_is_split_into_lines_using_charset() {
    assertThat(linesOf(SAMPLE_WIN_FILE, UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  void should_pass_if_mac_path_is_split_into_lines() {
    assertThat(linesOf(SAMPLE_MAC_FILE, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  void should_pass_if_mac_path_is_split_into_lines_using_charset() {
    assertThat(linesOf(SAMPLE_MAC_FILE, UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }
}
