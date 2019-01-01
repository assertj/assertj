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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.util.Strings.join;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import org.assertj.core.util.VisibleForTesting;

/**
 * An internal holder of the comparators for fields described with {@link FieldLocation}.
 */
public class FieldComparators {

  @VisibleForTesting
  Map<FieldLocation, Comparator<?>> fieldComparators;

  public FieldComparators() {
    fieldComparators = new TreeMap<>();
  }

  /**
   * Puts the {@code comparator} for the given {@code clazz}.
   *
   * @param fieldLocation the FieldLocation where to apply the comparator
   * @param comparator the comparator it self
   * @param <T> the type of the objects for the comparator
   */
  public <T> void register(FieldLocation fieldLocation, Comparator<? super T> comparator) {
    fieldComparators.put(fieldLocation, comparator);
  }

  /**
   * @return {@code true} is there are registered comparators, {@code false} otherwise
   */
  public boolean isEmpty() {
    return fieldComparators.isEmpty();
  }

  @Override
  public int hashCode() {
    return fieldComparators.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof FieldComparators && Objects.equals(fieldComparators, ((FieldComparators) obj).fieldComparators);
  }

  @Override
  public String toString() {
    List<String> registeredComparatorsDescription = new ArrayList<>();
    for (Entry<FieldLocation, Comparator<?>> fieldComparator : this.fieldComparators.entrySet()) {
      registeredComparatorsDescription.add(formatRegisteredComparator(fieldComparator));
    }
    return format("{%s}", join(registeredComparatorsDescription).with(", "));
  }

  private static String formatRegisteredComparator(Entry<FieldLocation, Comparator<?>> fieldComparator) {
    return format("%s -> %s", fieldComparator, fieldComparator.getValue());
  }

}
