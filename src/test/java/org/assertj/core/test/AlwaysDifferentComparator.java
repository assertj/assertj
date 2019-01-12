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
package org.assertj.core.test;

import java.sql.Timestamp;
import java.util.Comparator;

import org.assertj.core.groups.Tuple;

public class AlwaysDifferentComparator<T> implements Comparator<T> {

  public static final AlwaysDifferentComparator<Object> ALWAY_DIFFERENT = alwaysDifferent();
  public static final AlwaysDifferentComparator<String> ALWAY_DIFFERENT_STRING = alwaysDifferent();
  public static final AlwaysDifferentComparator<Timestamp> ALWAY_DIFFERENT_TIMESTAMP = alwaysDifferent();
  public static final AlwaysDifferentComparator<Tuple> ALWAY_DIFFERENT_TUPLE = alwaysDifferent();

  @Override
  public int compare(T o1, T o2) {
    return -1;
  }

  @Override
  public String toString() {
    return "AlwaysDifferentComparator";
  }

  public static <T> AlwaysDifferentComparator<T> alwaysDifferent() {
    return new AlwaysDifferentComparator<>();
  }
}
