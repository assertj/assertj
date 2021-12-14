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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * An internal holder of the comparators for fields described by their path without element index.
 * <p>
 * Examples: {@code name.first} or {@code names.first} but not {@code names[1].first} or {@code names.[1].first}
 */
public class FieldComparators extends FieldHolder<Comparator<?>> {

  /**
   * Puts the {@code comparator} for the given {@code fieldLocation}.
   *
   * @param fieldLocation the FieldLocation where to apply the comparator
   * @param comparator the comparator itself
   */
  public void registerComparator(String fieldLocation, Comparator<?> comparator) {
    super.put(fieldLocation, comparator);
  }

  /**
   * Checks, whether an any comparator is associated with the giving field location.
   *
   * @param fieldLocation the field location which association need to check
   * @return is field location contain a custom comparator
   */
  public boolean hasComparatorForField(String fieldLocation) {
    // TODO sanitize here?
    return super.hasEntity(fieldLocation);
  }

  /**
   * Retrieves a custom comparator, which is associated with the giving field location. If this location does not
   * associate with any custom comparators - this method returns null.
   *
   * @param fieldLocation the field location that has to be associated with a comparator
   * @return a custom comparator or null
   */
  public Comparator<?> getComparatorForField(String fieldLocation) {
    return super.get(fieldLocation);
  }

  /**
   * Returns a sequence of associated field-comparator pairs.
   *
   * @return sequence of field-comparator pairs
   */
  public Stream<Entry<String, Comparator<?>>> comparatorByFields() {
    return super.entryByField();
  }
}
