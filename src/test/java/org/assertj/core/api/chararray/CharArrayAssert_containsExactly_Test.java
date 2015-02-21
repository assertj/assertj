/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.chararray;

import static org.assertj.core.test.CharArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharArrayAssert;
import org.assertj.core.api.CharArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.CharArrayAssert#containsExactly(char...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class CharArrayAssert_containsExactly_Test extends CharArrayAssertBaseTest {

  @Override
  protected CharArrayAssert invoke_api_method() {
    return assertions.containsExactly('a', 'b');
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf('a', 'b'));
  }
}
