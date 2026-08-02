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
package org.assertj.core.error;

import java.util.Set;

import org.jspecify.annotations.Nullable;

/**
 * Creates an error message indicating that an assertion that verifies a map contains a key.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ShouldContainKeys extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainKeys}</code>.
   *
   * @param <K> key type
   * @param actual the actual value in the failed assertion.
   * @param keys the expected keys
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <K> ErrorMessageFactory shouldContainKeys(@Nullable Object actual, Set<K> keys) {
    if (keys.size() == 1) return new ShouldContainKeys(actual, keys.iterator().next());
    return new ShouldContainKeys(actual, keys);
  }

  private <K> ShouldContainKeys(@Nullable Object actual, Set<K> key) {
    super("%nExpecting actual:%n  %s%nto contain keys:%n  %s", actual, key);
  }

  private <K> ShouldContainKeys(@Nullable Object actual, K key) {
    super("%nExpecting actual:%n  %s%nto contain key:%n  %s", actual, key);
  }
}
