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
package org.assertj.core.api.path;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.BeforeAll;

/**
 * Tests for <code>{@link PathAssert#hasContent(String)}</code>.
 */
public class PathAssert_hasContent_Test extends PathAssertBaseTest {

  private static String expected;

  @BeforeAll
  public static void beforeOnce() {
    expected = "xyz";
  }

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.hasContent(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertHasContent(getInfo(assertions), getActual(assertions), expected, getCharset(assertions));
  }
}
