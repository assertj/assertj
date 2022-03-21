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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.Floats;
import org.assertj.core.test.AlwaysEqualComparator;

/**
 * Base class for {@link FloatAssert} tests.
 *
 * @author Olivier Michallat
 */
public abstract class FloatAssertBaseTest extends BaseTestTemplate<FloatAssert, Float> {

  protected static final AlwaysEqualComparator<Float> ALWAY_EQUAL_FLOAT = new AlwaysEqualComparator<>();

  protected Floats floats;
  protected Comparables comparables;

  @Override
  protected FloatAssert create_assertions() {
    return new FloatAssert(Float.valueOf(6.0f));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    floats = mock(Floats.class);
    assertions.floats = floats;
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  protected Floats getFloats(FloatAssert someAssertions) {
    return someAssertions.floats;
  }
}
