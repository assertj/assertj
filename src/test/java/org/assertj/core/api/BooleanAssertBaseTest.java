/*
 * Created on Aug 03, 2012
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.api.BooleanAssert;
import org.assertj.core.internal.Booleans;

import static org.mockito.Mockito.mock;

/**
 * Base class for {@link BooleanAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class BooleanAssertBaseTest extends BaseTestTemplate<BooleanAssert, Boolean> {
  protected Booleans booleans;

  @Override
  protected BooleanAssert create_assertions() {
    return new BooleanAssert(true);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    booleans = mock(Booleans.class);
    assertions.booleans = booleans;
  }

  protected Booleans getBooleans(BooleanAssert someAssertions) {
    return someAssertions.booleans;
  }
}
