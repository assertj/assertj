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
import static java.util.stream.Collectors.joining;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * {@link AssertFactory} decorator that casts the input value to the given type before invoking the decorated factory.
 *
 * @author Stefano Cordio
 *
 * @param <T>      the type to use for the cast.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 *
 * @since 3.13.0
 */
public class InstanceOfAssertFactory<T, ASSERT extends AbstractAssert<?, ?>> implements AssertFactory<Object, ASSERT> {

  private final Type type;
  private final Class<T> rawClass;
  private final AssertFactory<T, ASSERT> delegate;

  /**
   * Instantiates a new {@code InstanceOfAssertFactory} for a given type.
   *
   * @param type     the {@link Class} instance of the given type.
   * @param delegate the {@link AssertFactory} to decorate.
   */
  public InstanceOfAssertFactory(Class<T> type, AssertFactory<T, ASSERT> delegate) {
    this(type, type, delegate);
  }

  /**
   * Instantiates a new {@code InstanceOfAssertFactory} for a given type with type arguments.
   *
   * @param rawClass      the raw {@link Class} instance of the given type.
   * @param typeArguments the {@link Type} arguments of the given type.
   * @param delegate      the {@link AssertFactory} to decorate.
   * @since 3.26.0
   */
  public InstanceOfAssertFactory(Class<T> rawClass, Type[] typeArguments, AssertFactory<T, ASSERT> delegate) {
    this(new SyntheticParameterizedType(rawClass, typeArguments), rawClass, delegate);
  }

  private InstanceOfAssertFactory(Type type, Class<T> rawClass, AssertFactory<T, ASSERT> delegate) {
    this.type = requireNonNull(type, shouldNotBeNull("type")::create);
    this.rawClass = requireNonNull(rawClass, shouldNotBeNull("rawClass")::create);
    this.delegate = requireNonNull(delegate, shouldNotBeNull("delegate")::create);
  }

  Class<T> getRawClass() {
    return rawClass;
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
  public ASSERT createAssert(Object actual) {
    return delegate.createAssert(rawClass.cast(actual));
  }

  /**
   * Creates the custom {@link Assert} instance for the value provided by the
   * given {@code valueProvider}.
   * <p>
   * This is typically used by custom assertions that want to leverage existing
   * factories and need to manipulate the value upfront.
   * <p>
   * This implementation requests a value compatible with the {@link Type}
   * defined during instantiation and casts the provided value to the
   * corresponding raw {@link Class} before invoking the delegate.
   *
   * @param valueProvider the value provider for the {@code Assert} instance
   * @return the custom {@code Assert} instance for the provided value
   * @since 3.26.0
   */
  @Override
  public ASSERT createAssert(ValueProvider<?> valueProvider) {
    Object actual = valueProvider.apply(type);
    return createAssert(actual);
  }

  @Override
  public String toString() {
    return "InstanceOfAssertFactory for " + type.getTypeName();
  }

  private static final class SyntheticParameterizedType implements ParameterizedType, Serializable {

    private final Class<?> rawClass;
    private final Type[] typeArguments;

    public SyntheticParameterizedType(Class<?> rawClass, Type[] typeArguments) {
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
      if (!(other instanceof ParameterizedType)) {
        return false;
      }
      ParameterizedType otherType = (ParameterizedType) other;
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
