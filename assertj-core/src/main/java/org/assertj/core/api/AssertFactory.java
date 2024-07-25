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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * A factory that creates an {@link Assert} instance for a given value of type {@link T}.
 * <p>
 * The {@link #createAssert(Object)} factory method typically wraps a call to
 * {@link Assertions#assertThat}.
 * <p>
 * More advanced use cases can be achieved via {@link #createAssert(ValueProvider)},
 * where the provision of the value is externalized and the factory can require
 * explicit type compatibility for the value to be provided.
 *
 * @param <T> the type of the given value.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 * @see InstanceOfAssertFactory
 * @since 2.5.0 / 3.5.0
 */
@FunctionalInterface
public interface AssertFactory<T, ASSERT extends Assert<?, ?>> {

  /**
   * Creates the custom {@link Assert} instance for the given value.
   * <p>
   * Typically, this will just invoke <code>assertThat(actual)</code>.
   *
   * @param actual the input value for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the given value
   */
  ASSERT createAssert(T actual);

  /**
   * Creates the custom {@link Assert} instance for the value provided by the
   * given {@code valueProvider}.
   * <p>
   * This is typically used by custom assertions that want to leverage existing
   * factories and need to manipulate the value upfront.
   * <p>
   * The default implementation always requests a value compatible with {@code Object}.
   * <p>
   * Overriding implementations might provide a more specific {@link Type}
   * instance to express the desired type of the value returned by the provider.
   * When doing so, the factory is required to be consistent with the type parameter {@link T}.
   *
   * @param valueProvider the value provider for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the value provided by the given value provider
   * @since 3.26.0
   */
  default ASSERT createAssert(ValueProvider<? extends T> valueProvider) {
    T actual = valueProvider.apply(Object.class);
    return createAssert(actual);
  }

  /**
   * A provider that provides a value compatible with a given {@link Type}.
   * 
   * @param <T> the type of provided value.
   * @since 3.26.0
   */
  @FunctionalInterface
  interface ValueProvider<T> extends Function<Type, T> {

    /**
     * Provides a value compatible with the given {@code type}.
     * <p>
     * The compatibility definition depends on the actual {@code type} instance:
     * <ul>
     *   <li>If {@code type} is a {@link Class}, the provided value must be an instance of it.</li>
     *   <li>If {@code type} is a {@link ParameterizedType}, the provided value must be
     *   an instance of its {@link ParameterizedType#getRawType() raw type}.</li>
     * </ul>
     * All other {@link Type} subtypes are unsupported.
     *
     * @param type the given type, either a {@link Class} or a {@link ParameterizedType} instance
     * @return the provided value
     */
    T provide(Type type);

    /**
     * Provides a value compatible with the given {@code type}.
     *
     * @param type the given type
     * @return the provided value
     * @throws IllegalArgumentException if {@code type} is neither a {@link Class}
     * nor a {@link ParameterizedType} instance
     */
    default T apply(Type type) {
      if (!(type instanceof Class || type instanceof ParameterizedType)) {
        throw new IllegalArgumentException("Unsupported type: " + type.getClass());
      }
      return provide(type);
    }

  }

}
