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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Strings.join;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * An abstract field holder which provides to pair a specific entities for fields described by their path without
 * element index.
 *
 * @param <T> entity type
 */
abstract class FieldHolder<T> {

  protected final Map<String, T> fieldHolder;

  public FieldHolder() {
    fieldHolder = new TreeMap<>();
  }

  /**
   * Pairs the giving {@code entity} with the {@code fieldLocation}.
   *
   * @param fieldLocation the field location where to apply the giving entity
   * @param entity the entity to pair
   */
  public void put(String fieldLocation, T entity) {
    fieldHolder.put(fieldLocation, entity);
  }

  /**
   * Retrieves a specific entity which is associated with the giving {@code filedLocation} from the field holder, if it
   * presents. Otherwise, this method returns {@code null}.
   *
   * @param fieldLocation the field location which has to be associated with an entity
   * @return entity or null
   */
  public T get(String fieldLocation) {
    return fieldHolder.get(fieldLocation);
  }

  /**
   * Checks, whether an any entity associated with the giving field location.
   *
   * @param fieldLocation the field location which association need to check
   * @return is entity associated with field location
   */
  public boolean hasEntity(String fieldLocation) {
    return fieldHolder.containsKey(fieldLocation);
  }

  /**
   * @return {@code true} is there are registered entities, {@code false} otherwise
   */
  public boolean isEmpty() {
    return fieldHolder.isEmpty();
  }

  /**
   * Returns a sequence of all field-entry pairs which the current holder supplies.
   *
   * @return sequence of field-entry pairs
   */
  public Stream<Entry<String, T>> entryByField() {
    return fieldHolder.entrySet().stream();
  }

  @Override
  public String toString() {
    List<String> registeredEntitiesDescription = fieldHolder.entrySet().stream()
                                                            .map(FieldHolder::formatRegisteredEntity)
                                                            .collect(toList());
    return format("{%s}", join(registeredEntitiesDescription).with(", "));
  }

  private static <T> String formatRegisteredEntity(Entry<String, T> entry) {
    return format("%s -> %s", entry.getKey(), entry.getValue());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FieldHolder<?> that = (FieldHolder<?>) o;
    return fieldHolder.equals(that.fieldHolder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldHolder);
  }
}
