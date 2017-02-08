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

import org.assertj.core.internal.Comparables;


/**
 * Base class to test the concrete methods of {@link AbstractComparableAssert} (using a dummy implementation).
 * 
 * @author Olivier Michallat
 */
public abstract class AbstractComparableAssertBaseTest extends ComparableAssertBaseTest<ConcreteComparableAssert, Integer> {

  @Override
  protected ConcreteComparableAssert create_assertions() {
    return new ConcreteComparableAssert(8);
  }

  @Override
  protected Comparables getComparables(ConcreteComparableAssert someAssertions) {
    return someAssertions.comparables;
  }
}
