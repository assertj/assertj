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

import org.assertj.core.internal.DescribableComparator;

public class NeverEqualComparator<T> extends DescribableComparator<T> {

  public static final NeverEqualComparator<Object> NEVER_EQUALS = new NeverEqualComparator<>();
  public static final NeverEqualComparator<String> NEVER_EQUALS_STRING = new NeverEqualComparator<>();

  @Override
  public int compare(T o1, T o2) {
    return -1;
  }

  @Override
  public String description() {
    return NeverEqualComparator.class.getName();
  }
}
