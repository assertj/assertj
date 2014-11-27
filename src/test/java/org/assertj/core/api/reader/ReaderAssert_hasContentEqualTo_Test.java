/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.api.reader;

import static org.mockito.Mockito.verify;

import java.io.Reader;
import java.io.StringReader;

import org.assertj.core.api.ReaderAssert;
import org.assertj.core.api.ReaderAssertBaseTest;
import org.junit.BeforeClass;

/**
 * Tests for <code>{@link org.assertj.core.api.ReaderAssert#hasContentEqualTo(java.io.Reader)}</code>.
 *
 * @author Matthieu Baechler
 * @author Joel Costigliola
 * @author Bartosz Bierkowski
 */
public class ReaderAssert_hasContentEqualTo_Test extends ReaderAssertBaseTest {

  private static Reader expected;

  @BeforeClass
  public static void setUpOnce() {
    expected = new StringReader("b");
  }

  @Override
  protected ReaderAssert invoke_api_method() {
    return assertions.hasContentEqualTo(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(readers).assertEqualContent(getInfo(assertions), getActual(assertions), expected);
  }

}
