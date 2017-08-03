/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.test;

import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.google.common.reflect.TypeResolver;

/**
 * A Type canonizar that helps with the resolving of a {@link Type} so it can be compared to a similar one
 * considering generics.
 *
 * @author Filip Hrisafov
 * @author Clement Mathieu
 */
public class TypeCanonizer {

  /**
   * Returns a canonical form of {@code initialType} by replacing all {@link TypeVariable} by {@link Class}
   * instances.
   * <p>
   * <p>
   * Such a canonical form allows to compare {@link ParameterizedType}s, {@link WildcardType}(s),
   * {@link GenericArrayType}(s), {@link TypeVariable}(s).
   * </p>
   */
  public static Type canonize(Type initialType) {
    if (doesNotNeedCanonization(initialType)) {
      return initialType;
    }

    ReplacementClassSupplier replacementClassSupplier = new ReplacementClassSupplier();
    TypeResolver typeResolver = new TypeResolver();

    for (TypeVariable<?> typeVariable : findAllTypeVariables(initialType)) {
      // Once we have all TypeVariable we need to resolve them with actual classes so the typeResolver can resolve
      // them properly
      typeResolver = typeResolver.where(typeVariable, replacementClassSupplier.get());
    }

    return typeResolver.resolveType(initialType);
  }

  private static boolean doesNotNeedCanonization(Type type) {
    return !(type instanceof ParameterizedType
             || type instanceof GenericArrayType
             || type instanceof WildcardType
             || type instanceof TypeVariable);
  }

  /**
   * @param type the type for which all type variables need to be extracted
   * @return all {@code type}'s {@link TypeVariable}
   */
  private static Set<TypeVariable<?>> findAllTypeVariables(Type type) {
    Set<TypeVariable<?>> typeVariables = new LinkedHashSet<>();
    populateAllTypeVariables(typeVariables, type);
    return typeVariables;
  }

  /**
   * Adds all {@code type}'s {@link TypeVariable} to {@code typeVariables}
   *
   * @param typeVariables that need to be populated
   * @param types the types for which the {@link TypeVariable}(s) need to be extracted
   */
  private static void populateAllTypeVariables(Set<TypeVariable<?>> typeVariables, Type... types) {
    for (Type type : types) {
      if (type instanceof ParameterizedType) {
        populateAllTypeVariables(typeVariables, ((ParameterizedType) type).getActualTypeArguments());
      } else if (type instanceof WildcardType) {
        populateAllTypeVariables(typeVariables, ((WildcardType) type).getUpperBounds());
        populateAllTypeVariables(typeVariables, ((WildcardType) type).getLowerBounds());
      } else if (type instanceof GenericArrayType) {
        populateAllTypeVariables(typeVariables, ((GenericArrayType) type).getGenericComponentType());
      } else if (type instanceof TypeVariable) {
        typeVariables.add((TypeVariable<?>) type);
      }
    }
  }

  /**
   * Class that is used to supply replacement types for the canonization. The classes that are actually used have no
   * meaning. Any random classes can be used to do the canonization. Only the order of the classes is important.
   */
  private static class ReplacementClassSupplier {

    /**
     * Classes used as replacement types. The classes picked here are random classes, any class can be used.
     */
    static List<Class<?>> REPLACEMENT_TYPES = Arrays.asList(
      String.class, Integer.class, Exception.class, InputStream.class, System.class);

    private final Queue<Class<?>> classPool;

    private ReplacementClassSupplier() {
      this(REPLACEMENT_TYPES);
    }

    private ReplacementClassSupplier(Collection<Class<?>> classPool) {
      this.classPool = new ArrayDeque<>(classPool);
    }

    /**
     * @return a class which has not yet been returned
     * @throws IllegalStateException If the replacement class poll is exhausted
     */
    Class<?> get() {
      Class<?> clazz = classPool.poll();
      if (clazz == null) {
        throw new IllegalStateException("There are no more classes available. Consider increasing the class pool");
      }

      return clazz;
    }
  }
}
