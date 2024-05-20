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

import static java.nio.charset.Charset.defaultCharset;
import static org.mockito.Mockito.verify;

import java.io.File;
import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.jupiter.api.BeforeAll;

// tested in FileAssert_hasSameTextualContentAs_Test as hasSameContentAs is deprecated in favor of hasSameTextualContentAs
class FileAssert_hasSameContentAs_Test extends FileAssertBaseTest {

  private static File expected;

  @BeforeAll
  static void beforeOnce() {
    expected = new File("xyz");
  }

  @SuppressWarnings("deprecation")
  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasSameContentAs(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertSameContentAs(getInfo(assertions), getActual(assertions), defaultCharset(), expected, defaultCharset());
  }

}
