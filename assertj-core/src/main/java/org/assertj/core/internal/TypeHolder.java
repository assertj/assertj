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
package org.assertj.core.internal;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.DualClass.dualClass;
import static org.assertj.core.util.Strings.join;
import static org.assertj.core.util.introspection.ClassUtils.getAllInterfaces;
import static org.assertj.core.util.introspection.ClassUtils.getAllSuperclasses;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.assertj.core.util.ClassNameComparator;
import org.assertj.core.util.DualClass;
import org.assertj.core.util.DualClassComparator;

/**
 * An abstract type holder which provides to pair a specific entities for types.
 *
 * @param <T> entity type
 */
abstract class TypeHolder<T> {

  private static final Comparator<DualClass<?, ?>> DEFAULT_CLASS_COMPARATOR = new DualClassComparator(ClassNameComparator.INSTANCE,
                                                                                                      ClassNameComparator.INSTANCE);

  protected final Map<DualClass<?, ?>, T> typeHolder;

  public TypeHolder() {
    this(DEFAULT_CLASS_COMPARATOR);
  }

  public TypeHolder(Comparator<DualClass<?, ?>> comparator) {
    typeHolder = new TreeMap<>(requireNonNull(comparator, "Comparator must not be null"));
  }

  /**
   * This method returns the most relevant entity for the given class. The most relevant entity is the
   * entity which is registered for the class that is closest in the inheritance chain of the given {@code clazz}.
   * The order of checks is the following:
   * 1. If there is a registered entity for {@code clazz} then this one is used
   * 2. We check if there is a registered entity for a superclass of {@code clazz}
   * 3. We check if there is a registered entity for an interface of {@code clazz}
   *
   * @param clazz the class for which to find a entity
   * @return the most relevant entity, or {@code null} if on entity could be found
   */
  public T get(Class<?> clazz) {
    return get(clazz, null);
  }

  /**
   * This method returns the most relevant entity for the given class pair. The most relevant entity is the
   * entity which is registered for the class pair that is closest in the inheritance chain of the given {@code clazz} and
   * {@code otherClazz}.
   * The order of checks is the following:
   * 1. If there is a registered entity for {@code clazz} and {@code otherClazz} then this one is used
   * 2. We check if there is a registered entity for a superclass of {@code clazz}
   * 3. We check if there is a registered entity for an interface of {@code clazz}
   * 4. Found the closest registered class for {@code clazz},
   *    We check if there is a registered entity for a superclass of {@code otherClazz}
   * 5. We check if there is a registered entity for an interface of {@code otherClazz}
   *
   * @param clazz the class for which to find a entity
   * @param otherClazz the additional class for which to find a entity.
   * This may be {@code null} to find the entity bound only to the {@code clazz}
   * @return the most relevant entity, or {@code null} if on entity could be found
   */
  public T get(Class<?> clazz, Class<?> otherClazz) {
    Class<?> relevantType = getRelevantClass(clazz);
    if (relevantType == null) return null;
    return typeHolder.get(dualClass(relevantType, getRelevantClass(relevantType, otherClazz)));
  }

  /**
   * Puts the {@code entity} for the given {@code clazz}.
   *
   * @param clazz the class for the comparator
   * @param entity the entity itself
   */
  public void put(Class<?> clazz, T entity) {
    put(clazz, null, entity);
  }

  /**
   * Puts the {@code entity} for the given {@code clazz} and {@code otherClazz}.
   *
   * @param clazz the class for the comparator
   * @param otherClazz the additional class for the comparator. This may be {@code null} if the entity is bound only to the
   * {@code clazz}
   * @param entity the entity itself
   */
  public void put(Class<?> clazz, Class<?> otherClazz, T entity) {
    typeHolder.put(dualClass(clazz, otherClazz), entity);
  }

  /**
   * Checks, whether an entity is associated with the giving type.
   *
   * @param type the type for which to check an entity
   * @return is the giving type associated with any entity
   */
  public boolean hasEntity(Class<?> type) {
    return hasEntity(type, null);
  }

  /**
   * Checks, whether an entity is associated with the giving type pair.
   *
   * @param type the type for which to check an entity
   * @param otherType the additional type for which to check an entity
   * @return is the giving type associated with any entity
   */
  public boolean hasEntity(Class<?> type, Class<?> otherType) {
    return get(type, otherType) != null;
  }

  /**
   * @return {@code true} is there are registered entities, {@code false} otherwise
   */
  public boolean isEmpty() {
    return typeHolder.isEmpty();
  }

  /**
   * Removes all registered entities.
   */
  public void clear() {
    typeHolder.clear();
  }

  /**
   * Returns a sequence of all type-entity pairs which the current holder supplies.
   *
   * @return sequence of field-entity pairs
   */
  public Stream<Entry<DualClass<?, ?>, T>> entityByTypes() {
    return typeHolder.entrySet().stream();
  }

  /**
   * Returns the most relevant class for the given type from the giving collection of types.
   * <p>
   * The order of checks is the following:
   * <ol>
   * <li>If there is a registered message for {@code clazz} then this one is used</li>
   * <li>We check if there is a registered message for a superclass of {@code clazz}</li>
   * <li>We check if there is a registered message for an interface of {@code clazz}</li>
   * </ol>
   * If there is no relevant type in the giving collection - {@code null} will be returned.
   *
   * @param cls type to find a relevant class.
   * @return the most relevant class.
   */
  private Class<?> getRelevantClass(Class<?> cls) {
    Set<DualClass<?, ?>> keys = typeHolder.keySet();
    if (keys.stream().map(DualClass::actual).anyMatch(c -> c == cls)) return cls;

    for (Class<?> superClass : getAllSuperclasses(cls)) {
      if (keys.stream().map(DualClass::actual).anyMatch(c -> c == superClass)) return superClass;
    }
    for (Class<?> interfaceClass : getAllInterfaces(cls)) {
      if (keys.stream().map(DualClass::actual).anyMatch(c -> c == interfaceClass)) return interfaceClass;
    }
    return null;
  }

  private Class<?> getRelevantClass(Class<?> cls, Class<?> otherCls) {
    if (otherCls == null) return null;

    Set<DualClass<?, ?>> keys = typeHolder.keySet();
    if (keys.stream().anyMatch(c -> dualClass(cls, otherCls).equals(c))) return otherCls;

    for (Class<?> superClass : getAllSuperclasses(otherCls)) {
      if (keys.stream().anyMatch(c -> dualClass(cls, superClass).equals(c))) return superClass;
    }
    for (Class<?> interfaceClass : getAllInterfaces(otherCls)) {
      if (keys.stream().anyMatch(c -> dualClass(cls, interfaceClass).equals(c))) return interfaceClass;
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TypeHolder<?> that = (TypeHolder<?>) o;
    return typeHolder.equals(that.typeHolder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeHolder);
  }

  @Override
  public String toString() {
    List<String> registeredEntitiesDescription = typeHolder.entrySet().stream()
                                                           .map(TypeHolder::formatRegisteredEntity)
                                                           .collect(toList());
    return format("{%s}", join(registeredEntitiesDescription).with(", "));
  }

  private static <T> String formatRegisteredEntity(Entry<DualClass<?, ?>, T> entry) {
    if (entry.getKey().expected() == null) {
      return format("%s -> %s",
                    entry.getKey().actual().getSimpleName(),
                    entry.getValue());
    } else {
      return format("[%s - %s] -> %s",
                    entry.getKey().actual().getSimpleName(),
                    entry.getKey().expected().getSimpleName(),
                    entry.getValue());
    }
  }
}
