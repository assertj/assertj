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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FileAssert#usingCharset(String)}</code> when the provided string is invalid.
 * 
 * @author Olivier Michallat
 */
class FileAssert_usingCharset_String_invalid_Test extends FileAssertBaseTest {

  @Override
  @Test
  public void should_have_internal_effects() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertions.usingCharset("Klingon"))
                                        .withMessage("Charset:<'Klingon'> is not supported on this system");
  }

  @Override
  @Test
  public void should_return_this() {
    // Disable this test since the call fails
  }

  @Override
  protected FileAssert invoke_api_method() {
    // not used here
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // not used here
  }
}
