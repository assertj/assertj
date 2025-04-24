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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.CheckReturnValue;

/**
 * Base class for assertions that supports defining a comparator/BiPredicate to override the equals method of the type
 * under test.
 *
 * @param <SELF>   the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *                 target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *                 for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public abstract class AbstractAssertWithComparator<SELF extends AbstractAssertWithComparator<SELF, ACTUAL>, ACTUAL>
    extends AbstractAssert<SELF, ACTUAL> implements AssertWithComparator<SELF, ACTUAL> {

  // we prefer not to use Class<? extends S> selfType because it would force inherited
  // constructor to cast with a compiler warning
  // let's keep compiler warning internal (when we can) and not expose them to our end users.
  protected AbstractAssertWithComparator(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    // using a specific strategy to compare actual with other objects.
    this.objects = new Objects(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    // fall back to default strategy to compare actual with other objects.
    this.objects = Objects.instance();
    return myself;
  }

}
