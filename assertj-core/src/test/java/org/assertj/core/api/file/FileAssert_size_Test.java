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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ClasspathResources.resourceFile;

import java.io.File;

import org.junit.jupiter.api.Test;

class FileAssert_size_Test {

  @Test
  void should_be_able_to_use_file_assertions_on_file_size() {
    // GIVEN
    File file = resourceFile("actual_file.txt");
    // THEN
    then(file).size()
              .isGreaterThan(4L)
              .isLessThan(20L)
              .isBetween(1L, 10L)
              .returnToFile().hasContent("actual");
  }

  @Test
  void should_have_a_helpful_error_message_when_size_is_used_on_a_null_file() {
    // GIVEN
    File nullFile = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullFile).size().isGreaterThan(1))
                                    .withMessage("Can not perform assertions on the size of a null file.");
  }
}
