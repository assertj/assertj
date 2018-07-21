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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.inputstream;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.InputStreamAssert;
import org.assertj.core.api.InputStreamAssertBaseTest;
import org.junit.jupiter.api.BeforeAll;


/**
 * Tests for <code>{@link InputStreamAssert#hasContent(String)}</code>.
 *
 * @author Stephan Windmüller
 */
public class InputStreamAssert_hasContent_Test extends InputStreamAssertBaseTest {

  private static String expected;

  @BeforeAll
  public static void setUpOnce() {
    expected = "b";
  }

  @Override
  protected InputStreamAssert invoke_api_method() {
    return assertions.hasContent(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(inputStreams).assertHasContent(getInfo(assertions), getActual(assertions), expected);
  }

}
