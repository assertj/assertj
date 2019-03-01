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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.file;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;

import java.io.File;
import java.util.function.Predicate;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link FileAssert#isDirectoryNotContaining(Predicate)}</code>
 *
 * @author Valeriy Vyrva
 */
public class FileAssert_isDirectoryNotContaining_Predicate_Test extends FileAssertBaseTest {

  private final Predicate<File> filter = path -> path.getParent() != null;

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.isDirectoryNotContaining(filter);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertIsDirectoryNotContaining(getInfo(assertions), getActual(assertions), filter);
  }
}
