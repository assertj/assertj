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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.test.TestData.someIndex;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.assertj.core.data.Index;
import org.junit.Test;


/**
 * Tests for <code>{@link ByteArrayAssert#contains(int, Index)}</code>.
 */
public class ByteArrayAssert_contains_at_Index_with_Integer_Argument_Test extends ByteArrayAssertBaseTest {

  private Index index = someIndex();

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.contains(8, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContains(getInfo(assertions), getActual(assertions), 8, index);
  }

  @Test
  public void invoke_api_like_user() {
    assertThat(new byte[] { 1 }).contains(1, atIndex(0));
  }
}
