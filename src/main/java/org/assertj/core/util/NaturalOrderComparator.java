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
package org.assertj.core.util;

import java.nio.file.Path;
import java.util.Comparator;

import static java.lang.String.format;

public class NaturalOrderComparator<T extends Comparable<? super T>> extends NullSafeComparator<T> {

  private String description;

  public NaturalOrderComparator(Class<T> clazz) {
    this.description = format("lexicographic comparator (%s naturalComparator)", clazz.getSimpleName());
  }

  @Override
  protected int compareNonNull(T o1, T o2) {
    return Comparator.<T>naturalOrder().compare(o1, o2);
  }

  @Override
  public String toString() {
    return this.description;
  }

}
