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
package org.assertj.core.util;

import java.util.Comparator;

abstract class NullSafeComparator<T> implements Comparator<T> {

  @Override
  public int compare(T o1, T o2) {
    if (o1 == o2) return 0;
    if (o1 == null) return -1;
    if (o2 == null) return 1;

    return compareNonNull(o1, o2);
  }

  protected abstract int compareNonNull(T o1, T o2);
}
