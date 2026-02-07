/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.soft;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import org.assertj.core.api.Assert;
import org.assertj.core.api.AssertionErrorCollector;

/**
 * A factory that creates an {@link Assert} instance for a given value of type {@link T}.
 * <p>
 * More advanced use cases can be achieved via {@link #createSoftAssert(Object, AssertionErrorCollector)},
 * where the provision of the value is externalized and the factory can require
 * explicit type compatibility for the value to be provided.
 *
 * @param <T> the type of the given value.
 * @param <SOFT_ASSERT> the type of the resulting {@code SoftAssert}.
 * @since 4.0.0
 * @see DefaultSoftAssertFactory
 */
@FunctionalInterface
public interface SoftAssertFactory<T, SOFT_ASSERT extends SoftAssert> {

  /**
   * Creates the custom {@link Assert} instance for the given value.
   * <p>
   * Typically, this will just invoke {@code assertThat(actual)}.
   *
   * @param actual the input value for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the given value
   */
  SOFT_ASSERT createSoftAssert(T actual, AssertionErrorCollector assertionErrorCollector);

  /**
   * Creates the custom {@link Assert} instance for the value provided by the
   * given {@link ValueProvider}.
   * <p>
   * The given {@code ValueProvider} can execute type-aware logic before
   * providing the required value, like type validation or value conversion.
   * <p>
   * The default implementation always requests a value compatible with {@link Object}.
   * <p>
   * Overriding implementations might provide a more specific {@link Type}
   * instance to express the desired type of the value returned by the provider.
   * When doing so, the factory is required to be consistent with the type parameter {@link T}.
   *
   * @param valueProvider the value provider for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the provided value
   * @since 3.26.0
   */
  default SOFT_ASSERT createSoftAssert(ValueProvider<? extends T> valueProvider, AssertionErrorCollector assertionErrorCollector) {
    T actual = valueProvider.apply(Object.class);
    return createSoftAssert(actual, assertionErrorCollector);
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
