/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.testkit;

import java.sql.Timestamp;
import java.util.Comparator;

import org.assertj.core.groups.Tuple;

public class AlwaysEqualComparator<T> implements Comparator<T> {

  public static final AlwaysEqualComparator<Object> ALWAYS_EQUALS = alwaysEqual();
  public static final AlwaysEqualComparator<String> ALWAYS_EQUALS_STRING = alwaysEqual();
  public static final AlwaysEqualComparator<Timestamp> ALWAYS_EQUALS_TIMESTAMP = alwaysEqual();
  public static final AlwaysEqualComparator<Tuple> ALWAYS_EQUALS_TUPLE = alwaysEqual();

  @Override
  public int compare(T o1, T o2) {
    return 0;
  }

  @Override
  public String toString() {
    return "AlwaysEqualComparator";
  }

  public static <T> AlwaysEqualComparator<T> alwaysEqual() {
    return new AlwaysEqualComparator<>();
  }
}
