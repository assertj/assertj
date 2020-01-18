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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

/**
 * {@link AssertFactory} decorator which casts the input value to the given type before invoking the decorated {@link AssertFactory}.
 *
 * @param <T>      the type to use for the cast.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 *
 * @author Stefano Cordio
 * @since 3.13.0
 */
public class InstanceOfAssertFactory<T, ASSERT extends AbstractAssert<?, ?>> implements AssertFactory<Object, ASSERT> {

  private final Class<T> type;
  private final AssertFactory<T, ASSERT> assertFactory;

  /**
   * Instantiates a new {@code InstanceOfAssertFactory}.
   *
   * @param type          the {@code Class} instance of the given type.
   * @param assertFactory the {@code AssertFactory} to decorate.
   */
  public InstanceOfAssertFactory(Class<T> type, AssertFactory<T, ASSERT> assertFactory) {
    this.type = requireNonNull(type, shouldNotBeNull("type").create());
    this.assertFactory = requireNonNull(assertFactory, shouldNotBeNull("assertFactory").create());
  }

  Class<T> getType() {
    return type;
  }

  /** {@inheritDoc} */
  @Override
  public ASSERT createAssert(Object value) {
    return assertFactory.createAssert(type.cast(value));
  }

  @Override
  public String toString() {
    return type.getSimpleName() + " InstanceOfAssertFactory";
  }
}
