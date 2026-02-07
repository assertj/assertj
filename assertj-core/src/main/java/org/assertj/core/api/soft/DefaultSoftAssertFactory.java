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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import org.assertj.core.api.Assert;
import org.assertj.core.api.AssertionErrorCollector;

/**
 * {@link SoftAssertFactory} decorator that casts the input value to the given type before invoking the decorated factory.
 *
 * @param <T>      the type to use for the cast
 * @param <SOFT_ASSERT> the type of the resulting {@link Assert}
 */
public class DefaultSoftAssertFactory<T, SOFT_ASSERT extends SoftAssert> implements SoftAssertFactory<Object, SOFT_ASSERT> {

  private final Type type;
  private final Class<T> rawClass;
  private final SoftAssertFactory<T, SOFT_ASSERT> delegate;

  /**
   * Instantiates a new {@code InstanceOfSoftAssertFactory} for a given type.
   *
   * @param type     the {@link Class} instance of the given type
   * @param delegate the {@link SoftAssertFactory} to decorate
   */
  public DefaultSoftAssertFactory(Class<T> type, SoftAssertFactory<T, SOFT_ASSERT> delegate) {
    this(type, type, delegate);
  }

  /**
   * Instantiates a new {@code InstanceOfSoftAssertFactory} for a given type
   * with type arguments, usually representing a {@link ParameterizedType}.
   *
   * @param rawClass      the raw {@link Class} instance of the given type
   * @param typeArguments the {@link Type} arguments of the given type
   * @param delegate      the {@link SoftAssertFactory} to decorate
   * @since 4.0.0
   */
  public DefaultSoftAssertFactory(Class<T> rawClass, Type[] typeArguments, SoftAssertFactory<T, SOFT_ASSERT> delegate) {
    this(new SyntheticParameterizedType(rawClass, typeArguments), rawClass, delegate);
  }

  private DefaultSoftAssertFactory(Type type, Class<T> rawClass, SoftAssertFactory<T, SOFT_ASSERT> delegate) {
    this.type = requireNonNull(type, shouldNotBeNull("type")::create);
    this.rawClass = requireNonNull(rawClass, shouldNotBeNull("rawClass")::create);
    this.delegate = requireNonNull(delegate, shouldNotBeNull("delegate")::create);
  }

  /**
   * Creates the custom {@link Assert} instance for the given value.
   * <p>
   * Before invoking the delegate, the factory casts the value to the raw
   * {@link Class} defined during instantiation.
   *
   * @param actual the input value for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the given value
   */
  @Override
  public SOFT_ASSERT createSoftAssert(Object actual, AssertionErrorCollector assertionErrorCollector) {
    return delegate.createSoftAssert(rawClass.cast(actual), assertionErrorCollector);
  }

  /**
   * Creates the custom {@link Assert} instance for the value provided by the
   * given {@link ValueProvider}.
   * <p>
   * The given {@code ValueProvider} can execute type-aware logic before
   * providing the required value, like type validation or value conversion.
   * <p>
   * This implementation requests a value compatible with the {@link Type}
   * defined during instantiation and casts the provided value to the
   * corresponding raw {@link Class} before invoking the delegate.
   *
   * @param valueProvider the value provider for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the provided value
   * @since 4.0.0
   */
  @Override
  public SOFT_ASSERT createSoftAssert(ValueProvider<?> valueProvider, AssertionErrorCollector assertionErrorCollector) {
    Object actual = valueProvider.apply(type);
    return createSoftAssert(actual, assertionErrorCollector);
  }

  @Override
  public String toString() {
    return "InstanceOfSoftAssertFactory for " + type.getTypeName();
  }

  private static final class SyntheticParameterizedType implements ParameterizedType, Serializable {

    private final Class<?> rawClass;
    private final Type[] typeArguments;

    private SyntheticParameterizedType(Class<?> rawClass, Type[] typeArguments) {
      this.rawClass = requireNonNull(rawClass, shouldNotBeNull("rawClass")::create);
      this.typeArguments = requireNonNull(typeArguments, shouldNotBeNull("typeArguments")::create);
    }

    @Override
    public String getTypeName() {
      return rawClass.getTypeName() + Arrays.stream(typeArguments)
                                            .map(Type::getTypeName)
                                            .collect(joining(", ", "<", ">"));
    }

    @Override
    public Type getOwnerType() {
      return null;
    }

    @Override
    public Class<?> getRawType() {
      return rawClass;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return typeArguments;
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof ParameterizedType otherType)) {
        return false;
      }
      return (otherType.getOwnerType() == null && rawClass.equals(otherType.getRawType()) &&
              Arrays.equals(typeArguments, otherType.getActualTypeArguments()));
    }

    @Override
    public int hashCode() {
      return Objects.hash(rawClass, Arrays.hashCode(typeArguments));
    }

    @Override
    public String toString() {
      return getTypeName();
    }

  }

}
