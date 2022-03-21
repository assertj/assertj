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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.file;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.junit.jupiter.api.Test;

class FileAssert_content_Test extends FileAssertBaseTest implements NavigationMethodBaseTest<FileAssert> {

  @Override
  protected FileAssert invoke_api_method() {
    assertions.content();
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
  public void should_return_StringAssert_on_path_content() {
    // GIVEN
    File file = new File("src/test/resources/actual_file.txt");
    // WHEN
    AbstractStringAssert<?> stringAssert = assertThat(file).content();
    // THEN
    stringAssert.isEqualTo(format("actual%n"));
  }

  @Override
  public FileAssert getAssertion() {
    return assertions;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(FileAssert assertion) {
    return assertion.content();
  }

}
