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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FileAssert Content")
class FileAssert_content_Test {

  private static File actual;

  @BeforeAll
  static void setUpOnce() {
    actual = new File("src/test/resources/actual_file.txt");
  }

  @Test
  void should_return_actual_content() throws IOException {
    // WHEN
    String fileContent = new String(java.nio.file.Files.readAllBytes(actual.toPath()));
    // THEN
    assertThat(actual).content()
                      .isEqualTo(fileContent);
  }

  @Test
  void should_pass_if_has_proper_functionality() {
    // GIVEN
    String prefix = "ac";
    String subString = "ct";
    // THEN
    assertThat(actual).content()
                      .startsWith(prefix)
                      .contains(subString);
  }
}
