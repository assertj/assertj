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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

class Assertions_linesOf_Test {

  private static final List<String> EXPECTED_CONTENT = newArrayList("A text file encoded in UTF-8, with diacritics:", "é à");

  @Test
  void should_read_lines_of_file_with_UTF8_charset() {
    File file = new File("src/test/resources/utf8.txt");
    assertThat(linesOf(file, "UTF-8")).isEqualTo(EXPECTED_CONTENT);
    assertThat(linesOf(file, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  void should_read_lines_of_path_with_UTF8_charset() {
    Path path = Paths.get("src", "test", "resources", "utf8.txt");
    assertThat(linesOf(path, "UTF-8")).isEqualTo(EXPECTED_CONTENT);
    assertThat(linesOf(path, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

}
