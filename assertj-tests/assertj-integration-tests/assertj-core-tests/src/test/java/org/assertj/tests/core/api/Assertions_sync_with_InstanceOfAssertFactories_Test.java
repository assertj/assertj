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
package org.assertj.tests.core.api;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.assertj.core.data.MapEntry.entry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.Object2DArrayAssert;
import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

class Assertions_sync_with_InstanceOfAssertFactories_Test extends BaseAssertionsTest {

  private static final Class<?>[] IGNORED_INPUT_TYPES = {
      // There is no dedicated `assertThat`.
      Set.class,
      Temporal.class
  };

  private static final Class<?>[] IGNORED_ASSERT_TYPES_FOR_FIELD_FACTORIES = {
      // There cannot be a `Comparable` field factory with a base type.
      AbstractComparableAssert.class,
      // The comparison of the input `GenericArrayTypes` will always fail, since it verifies the inner `TypeVariable`
      // which returns the defining `Method` as a result of `TypeVariable#getGenericDeclaration()`.
      ObjectArrayAssert.class, Object2DArrayAssert.class,
      // A field factory for an object is pointless.
      ObjectAssert.class,
  };

  private static final Class<?>[] IGNORED_ASSERT_TYPES_FOR_METHOD_FACTORIES = {
      // The comparison of the input `GenericArrayTypes` will always fail, since it verifies the inner `TypeVariable`
      // which returns the defining `Method` as a result of `TypeVariable#getGenericDeclaration()`.
      ObjectArrayAssert.class, Object2DArrayAssert.class,
  };

  @Test
  void each_standard_assertion_should_have_an_instance_of_assert_factory_static_field() {
    // GIVEN
    Map<Type, Type> assertThatMethods = findAssertThatParameterAndReturnTypes();
    // WHEN
    Map<Type, Type> fieldFactories = findFieldFactoryTypes();
    // THEN
    then(fieldFactories).containsExactlyInAnyOrderEntriesOf(assertThatMethods);
  }

  @Test
  void each_standard_assertion_with_type_parameters_should_have_an_instance_of_assert_factory_static_method() {
    // GIVEN
    Map<Type, Type> assertThatMethods = findTypedAssertThatParameterAndReturnTypes();
    // WHEN
    Map<Type, Type> methodFactories = findMethodFactoryTypes();
    // THEN
    then(methodFactories).containsExactlyInAnyOrderEntriesOf(assertThatMethods);
  }

  private Map<Type, Type> findAssertThatParameterAndReturnTypes() {
    return Stream.of(findMethodsWithName(Assertions.class, "assertThat",
                                         ignoredReturnTypes(IGNORED_ASSERT_TYPES_FOR_FIELD_FACTORIES)))
                 .map(this::toParameterAndReturnTypeEntry)
                 .filter(not(this::isPrimitiveTypeKey))
                 .collect(toMap(Entry::getKey, Entry::getValue));
  }

  private <K, V> boolean isPrimitiveTypeKey(Entry<K, V> entry) {
    if (entry.getKey() instanceof Class) {
      return ((Class<?>) entry.getKey()).isPrimitive();
    }
    return false;
  }

  private Map<Type, Type> findTypedAssertThatParameterAndReturnTypes() {
    return Stream.of(findMethodsWithName(Assertions.class, "assertThat",
                                         ignoredReturnTypes(IGNORED_ASSERT_TYPES_FOR_METHOD_FACTORIES)))
                 .filter(this::hasTypeParameters)
                 .map(this::toParameterAndReturnTypeEntry)
                 .collect(toMap(Entry::getKey, Entry::getValue));
  }

  private boolean hasTypeParameters(Method method) {
    return method.getTypeParameters().length != 0;
  }

  private Class<?>[] ignoredReturnTypes(Class<?>... ignoredAssertTypes) {
    return Stream.of(SPECIAL_IGNORED_RETURN_TYPES, ignoredAssertTypes)
                 .flatMap(Stream::of)
                 .toArray(Class[]::new);
  }

  private Entry<Type, Type> toParameterAndReturnTypeEntry(Method method) {
    return entry(getRawType(genericParameterType(method)), getRawType(method.getGenericReturnType()));
  }

  private Type genericParameterType(Method method) {
    Type[] parameterTypes = method.getGenericParameterTypes();
    assertThat(parameterTypes).hasSize(1);
    return parameterTypes[0];
  }

  private Map<Type, Type> findFieldFactoryTypes() {
    return Stream.of(InstanceOfAssertFactories.class.getFields())
                 .filter(not(Field::isSynthetic)) // Exclude $jacocoData - see #590 and jacoco/jacoco#168
                 .map(Field::getGenericType)
                 .map(this::extractTypeParameters)
                 .filter(not(this::isIgnoredInputType))
                 .filter(not(this::isIgnoredFieldFactory))
                 .collect(toMap(Entry::getKey, Entry::getValue));
  }

  private boolean isIgnoredFieldFactory(Entry<Type, Type> e) {
    return isIgnoredFactory(e, IGNORED_ASSERT_TYPES_FOR_FIELD_FACTORIES);
  }

  private Map<Type, Type> findMethodFactoryTypes() {
    return Stream.of(InstanceOfAssertFactories.class.getMethods())
                 .map(Method::getGenericReturnType)
                 .map(this::extractTypeParameters)
                 .filter(not(this::isIgnoredInputType))
                 .filter(not(this::isIgnoredMethodFactory))
                 .collect(toMap(Entry::getKey, Entry::getValue));
  }

  private boolean isIgnoredMethodFactory(Entry<Type, Type> e) {
    return isIgnoredFactory(e, IGNORED_ASSERT_TYPES_FOR_METHOD_FACTORIES);
  }

  private boolean isIgnoredFactory(Entry<Type, Type> e, Class<?>[] ignoredTypes) {
    return Stream.of(ignoredTypes).anyMatch(type -> e.getValue().equals(type));
  }

  private boolean isIgnoredInputType(Entry<Type, Type> e) {
    return Stream.of(IGNORED_INPUT_TYPES).anyMatch(type -> e.getKey().equals(type));
  }

  private Entry<Type, Type> extractTypeParameters(Type type) {
    assertThat(type).asInstanceOf(type(ParameterizedType.class))
                    .returns(InstanceOfAssertFactory.class, from(ParameterizedType::getRawType))
                    .extracting(ParameterizedType::getActualTypeArguments, as(ARRAY))
                    .hasSize(2);
    Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
    return entry(getRawType(typeArguments[0]), getRawType(typeArguments[1]));
  }

  private Type getRawType(Type type) {
    if (type instanceof ParameterizedType parameterizedType) {
      return parameterizedType.getRawType();
    } else if (type instanceof TypeVariable<?> typeVariable) {
      Type[] bounds = typeVariable.getBounds();
      assertThat(bounds).hasSize(1);
      return getRawType(bounds[0]);
    }
    return type;
  }

}
