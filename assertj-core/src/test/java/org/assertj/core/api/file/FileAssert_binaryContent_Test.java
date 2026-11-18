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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.testkit.ClasspathResources.resourceFile;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.assertj.core.api.FileAssert;
import org.assertj.core.api.NavigationMethodWithComparatorBaseTest;
import org.junit.jupiter.api.Test;

class FileAssert_binaryContent_Test implements NavigationMethodWithComparatorBaseTest<FileAssert> {

  @Test
  void should_return_ByteArrayAssert_on_path_content() {
    // GIVEN
    File file = resourceFile("actual_file.txt");
    // WHEN
    AbstractByteArrayAssert<?> byteArrayAssert = assertThat(file).binaryContent();
    // THEN
    byteArrayAssert.asString(UTF_8).isEqualTo("actual%n".formatted());
  }

  @Test
  void should_fail_if_actual_is_not_readable() {
    // GIVEN
    File actual = new File("does-not-exist");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).binaryContent());
    // THEN
    then(assertionError).hasMessage(shouldBeReadable(actual).create());
  }

  @Override
  public FileAssert getAssertion() {
    return new FileAssert(resourceFile("actual_file.txt"));
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(FileAssert assertion) {
    return assertion.binaryContent();
  }

}
