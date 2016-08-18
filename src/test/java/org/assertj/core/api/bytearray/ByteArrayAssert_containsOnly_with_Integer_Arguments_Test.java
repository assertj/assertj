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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ByteArrayAssert;
import org.junit.Test;


/**
 * Tests for <code>{@link ByteArrayAssert#containsOnly(int...)}</code>.
 */
public class ByteArrayAssert_containsOnly_with_Integer_Arguments_Test {

  @Test
  public void invoke_api_like_user() {
    assertThat(new byte[] { 1, 2, 3 }).containsOnly(3, 2, 1);
  }
}
