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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

/**
 * Base class for all array assertions.
 *
 * @author Joel Costigliola
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <ACTUAL> the type of the "actual" value which is an Array of ELEMENT.
 * @param <ELEMENT> the type of the "actual" array element.
 */
public abstract class AbstractArrayAssert<SELF extends AbstractArrayAssert<SELF, ACTUAL, ELEMENT>, ACTUAL, ELEMENT>
    extends AbstractEnumerableAssert<SELF, ACTUAL, ELEMENT>
    implements ArraySortedAssert<AbstractArrayAssert<SELF, ACTUAL, ELEMENT>, ELEMENT> {

  protected AbstractArrayAssert(final ACTUAL actual, final Class<?> selfType) {
    super(actual, selfType);
  }

  static void requireNonNullParameter(Object parameter, String parameterName) {
    requireNonNull(parameter, shouldNotBeNull(parameterName)::create);
  }

}
