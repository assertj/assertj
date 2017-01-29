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
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Bytes;


/**
 * Base class for {@link ByteAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class ByteAssertBaseTest extends BaseTestTemplate<ByteAssert, Byte> {
  protected Bytes bytes;

  @Override
  protected ByteAssert create_assertions() {
    return new ByteAssert((byte) 0);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    bytes = mock(Bytes.class);
    assertions.bytes = bytes;
  }

  protected Bytes getBytes(ByteAssert someAssertions) {
    return someAssertions.bytes;
  }
}
