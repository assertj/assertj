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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThatFile;

import java.io.File;

import org.assertj.core.api.AbstractFileAssert;
import org.junit.jupiter.api.Test;

class Assertions_assertThatFile_Test {

  @Test
  void should_accept_File() {
    // GIVEN
    File actual = new File("test");
    // WHEN
    AbstractFileAssert<?> result = assertThatFile(actual);
    // THEN
    result.hasName("test");
  }

  @Test
  void should_accept_bounded_generic_File_subtype() {
    // WHEN
    AbstractFileAssert<?> result = assertThatFile(getFileSubtype());
    // THEN
    result.isNotNull();
  }

  @SuppressWarnings("unchecked")
  private static <T extends File> T getFileSubtype() {
    return (T) new File("test");
  }

}
