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

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * A functional interface to create an {@link Assert} for a given value.
 * <p>
 * The factory typically wraps a call to <code>assertThat(actual)</code>
 * to produce a concrete assert type {@code ASSERT} for the input element of type {@code T}.
 * <p>
 * This interface is typically used by navigation assertions on iterable types like {@link AbstractIterableAssert}
 * when calling {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable&lt;E&gt;, AssertFactory&lt;E, ASSERT&gt;)}
 * <p>
 * @param <T> the type of the input to the factory.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 *
 * @since 2.5.0 / 3.5.0
 */
@FunctionalInterface
public interface AssertFactory<T, ASSERT extends Assert<?, ?>> {

  /**
   * Create the custom {@link Assert} instance for the given element value.
   * <p>
   * Typically, this will just invoke <code>assertThat(actual)</code>
   * @param actual the input value for the {@code Assert} instance
   * @return returns the custom {@code Assert} instance for the given element value
   */
  ASSERT createAssert(T actual);

  /**
   * Return the {@link Type} of the input this factory expects, or an empty {@code Optional}
   * if the information is not available.
   * 
   * @implSpec Overriding implementations providing a non-empty optional are required to
   * return a {@code Type} instance consistent with the value of the factory type parameter {@link T}.
   * 
   * @implNote The default implementation always returns an empty {@code Optional}.
   * 
   * @return the expected type, or an empty {@code Optional} if the information is not available.
   * @since 3.26.0
   */
  default Optional<Type> getType() {
    return Optional.empty();
  }

}
