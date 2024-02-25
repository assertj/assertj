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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link AssertFactory} decorator which casts the input value to the given type before invoking the decorated factory.
 *
 * @param <T>      the type to use for the cast.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 *
 * @author Stefano Cordio
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

  public Type getType() {
    return type;
  }

  public Class<T> getRawClass() {
    return rawClass;
  }

  /** {@inheritDoc} */
  @Override
  public ASSERT createAssert(Object actual) {
    return delegate.createAssert(rawClass.cast(actual));
  }

  @Override
  public String toString() {
    return "InstanceOfAssertFactory for " + type.getTypeName();
  }

  private static final class SyntheticParameterizedType implements ParameterizedType, Serializable {

    private final Class<?> rawType;
    private final Type[] typeArguments;

    public SyntheticParameterizedType(Class<?> rawType, Type[] typeArguments) {
      this.rawType = rawType;
      this.typeArguments = typeArguments;
    }

    @Override
    public String getTypeName() {
      return rawType.getTypeName() + Arrays.stream(typeArguments)
                                           .map(Type::getTypeName)
                                           .collect(Collectors.joining(", ", "<", ">"));
    }

    @Override
    public Type getOwnerType() {
      return null;
    }

    @Override
    public Class<?> getRawType() {
      return rawType;
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
      return (otherType.getOwnerType() == null && rawType.equals(otherType.getRawType()) &&
              Arrays.equals(typeArguments, otherType.getActualTypeArguments()));
    }

    @Override
    public int hashCode() {
      return Objects.hash(rawType, Arrays.hashCode(typeArguments));
    }

    @Override
    public String toString() {
      return getTypeName();
    }
  }

}
