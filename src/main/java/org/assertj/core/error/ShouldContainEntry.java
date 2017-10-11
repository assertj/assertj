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

import java.util.Map;

import org.assertj.core.api.Condition;

/**
 * Creates an error message indicating that an assertion that verifies a map contains an entry..
 */
public class ShouldContainEntry extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainEntry}</code>.
   *
   * @param <K> key type
   * @param <V> value type
   * @param actual the actual map in the failed assertion.
   * @param entryCondition entry condition.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <K, V> ErrorMessageFactory shouldContainEntry(Map<K, V> actual, Condition<?> entryCondition) {
    return new ShouldContainEntry(actual, entryCondition);
  }

  /**
   * Creates a new <code>{@link ShouldContainEntry}</code>.
   *
   * @param <K> key type
   * @param <V> value type
   * @param actual the actual map in the failed assertion.
   * @param keyCondition key condition.
   * @param valueCondition value condition.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <K, V> ErrorMessageFactory shouldContainEntry(Map<K, V> actual, Condition<? super K> keyCondition,
                                                              Condition<? super V> valueCondition) {
    return new ShouldContainEntry(actual, keyCondition, valueCondition);
  }

  private <K, V> ShouldContainEntry(Map<K, V> actual, Condition<?> entryCondition) {
    super("%n" +
          "Expecting:%n" +
          " <%s>%n" +
          "to contain an entry satisfying:%n" +
          " <%s>",
          actual, entryCondition);
  }

  private <K, V> ShouldContainEntry(Map<K, V> actual, Condition<? super K> keyCondition,
                                    Condition<? super V> valueCondition) {
    super("%n" +
          "Expecting:%n" +
          " <%s>%n" +
          "to contain an entry satisfying both key and value conditions:%n" +
          "- key condition:%n" +
          "    <%s>%n" +
          "- value condition:%n" +
          "    <%s>",
          actual, keyCondition, valueCondition);
  }
}
