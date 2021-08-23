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

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class FileAssert_size_Test {

  @Test
  void should_be_able_to_use_file_assertions_on_file_size() {

    File file = new File("src/test/resources/actual_file.txt");

    assertThat(file).size()
      .isGreaterThan(4L)
      .isLessThan(8L)
      .isEqualTo(7L)
      .isBetween(1L, 7L)
      .returnToFile().hasContent("actual");
  }

  @Test
  void should_have_a_helpful_error_message_when_size_is_used_on_a_null_file() {
    File nullFile = null;
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullFile).size().isGreaterThan(1))
      .withMessage("Can not perform assertions on the size of a null file.");
  }
}
