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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.junit.jupiter.api.Test;

class FileAssert_binaryContent_Test extends FileAssertBaseTest implements NavigationMethodBaseTest<FileAssert> {

  @Override
  protected FileAssert invoke_api_method() {
    assertions.binaryContent();
    return assertions;
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertCanRead(getInfo(assertions), getActual(assertions));
  }

  @Override
  protected FileAssert create_assertions() {
    return new FileAssert(new File("src/test/resources/actual_file.txt"));
  }

  @Test
  void should_return_ByteArrayAssert_on_path_content() {
    // GIVEN
    File file = new File("src/test/resources/actual_file.txt");
    // WHEN
    AbstractByteArrayAssert<?> byteArrayAssert = assertThat(file).binaryContent();
    // THEN
    byteArrayAssert.asString(StandardCharsets.UTF_8).isEqualTo(format("actual%n"));
  }

  @Override
  public FileAssert getAssertion() {
    return assertions;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(FileAssert assertion) {
    return assertion.binaryContent();
  }

}
