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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.inputstream;

import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;

import org.assertj.core.api.InputStreamAssert;
import org.assertj.core.api.InputStreamAssertBaseTest;

/**
 * Tests for <code>{@link InputStreamAssert#isEmpty()}</code>.
 *
 * @author Sára Juhošová
 */
class InputStreamAssert_InputStreamException_Test extends InputStreamAssertBaseTest {

  @Override
  protected InputStreamAssert create_assertions() {
    return new InputStreamAssert(new ByteArrayInputStream(new byte[] { 'a' }));
  }

  @Override
  protected InputStreamAssert invoke_api_method() {
    return assertions.isEmpty();
  }

  @Override
  protected void verify_internal_effects() {
    verify(inputStreams).assertIsEmpty(getInfo(assertions), getActual(assertions));
  }

}
