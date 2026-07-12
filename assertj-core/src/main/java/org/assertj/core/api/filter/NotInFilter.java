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
package org.assertj.core.api.filter;

/** Filter excluding a set of candidate values. */
public class NotInFilter extends FilterOperator<Object[]> {

  private NotInFilter(Object... filterParameter) {
    super(filterParameter);
  }

  /**
   * Creates a filter excluding the given values.
   *
   * @param valuesNotToMatch the values to exclude
   * @return the exclusion filter
   */
  public static NotInFilter notIn(Object... valuesNotToMatch) {
    return new NotInFilter(valuesNotToMatch);
  }

  // could be removed but since it is part of the API we can't ...
  /**
   * Legacy filter method retained for compatibility.
   *
   * @param propertyValueOfCurrentElement the current property value
   * @return always {@code false}
   */
  public boolean filter(@SuppressWarnings("unused") Object propertyValueOfCurrentElement) {
    return false;
  }

  @Override
  public <E> Filters<E> applyOn(Filters<E> filters) {
    return filters.notIn(filterParameter);
  }
}