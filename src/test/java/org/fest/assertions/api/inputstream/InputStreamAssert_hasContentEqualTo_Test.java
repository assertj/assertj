/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api.inputstream;

import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;

import org.fest.assertions.api.InputStreamAssert;
import org.fest.assertions.api.InputStreamAssertBaseTest;

/**
 * Tests for <code>{@link InputStreamAssert#hasContentEqualTo(InputStream)}</code>.
 * 
 * @author Matthieu Baechler
 * @author Joel Costigliola
 */
public class InputStreamAssert_hasContentEqualTo_Test extends InputStreamAssertBaseTest {

  private static InputStream expected;

  @BeforeClass
  public static void setUpOnce() {
    expected = new ByteArrayInputStream(new byte[] { 'b' });
  }

  @Override
  protected InputStreamAssert invoke_api_method() {
    return assertions.hasContentEqualTo(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(inputStreams).assertEqualContent(getInfo(assertions), getActual(assertions), expected);
  }

}
