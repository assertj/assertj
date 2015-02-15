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
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Shorts;


/**
 * Base class for {@link ShortAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class ShortAssertBaseTest extends BaseTestTemplate<ShortAssert, Short> {
  protected Shorts shorts;

  @Override
  protected ShortAssert create_assertions() {
    return new ShortAssert((short) 6);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    shorts = mock(Shorts.class);
    assertions.shorts = shorts;
  }

  protected Shorts getShorts(ShortAssert someAssertions) {
    return someAssertions.shorts;
  }
}
