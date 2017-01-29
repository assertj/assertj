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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;

public class Assertions_linesOf_Test {

  private static final List<String> EXPECTED_CONTENT = newArrayList("A text file encoded in UTF-8, with diacritics:", "é à");

  @Test
  public void should_read_lines_of_file_with_UTF8_charset() {
    File file = new File("src/test/resources/utf8.txt");
    assertThat(linesOf(file, "UTF-8")).isEqualTo(EXPECTED_CONTENT);
    assertThat(linesOf(file, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

}
