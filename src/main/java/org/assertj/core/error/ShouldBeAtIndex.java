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
package org.assertj.core.error;

import java.util.List;

import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;


/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains a value at a given index that
 * satisfies a <code>{@link Condition}</code> failed.
 * 
 * @author Bo Gotthardt
 */
public class ShouldBeAtIndex extends BasicErrorMessageFactory {
  /**
   * Creates a new <code>{@link ShouldBeAtIndex}</code>.
   * @param <T> guarantees that the type of the actual value and the generic type of the {@code Condition} are the same.
   * @param actual the actual value in the failed assertion.
   * @param condition the {@code Condition}.
   * @param index the index of the expected value.
   * @param found the value in {@code actual} stored under {@code index}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeAtIndex(List<? extends T> actual, Condition<? super T> condition, Index index, T found) {
    return new ShouldBeAtIndex(actual, condition, index, found);
  }

  private <T> ShouldBeAtIndex(List<? extends T> actual, Condition<? super T> condition, Index index, T found) {
    super("%nExpecting:%n <%s>%nat index <%s> to be:%n <%s>%nin:%n <%s>%n", found, index.value, condition, actual);
  }
}
