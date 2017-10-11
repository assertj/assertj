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

import static org.assertj.core.error.ShouldNotContainKey.shouldNotContainKey;

import java.util.Set;

/**
 * Creates an error message indicating that an assertion that verifies a map does not contain keys.
 *
 * @author dorzey
 */
public class ShouldNotContainKeys extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotContainKeys}</code>.
   *
   * @param <K> key type
   * @param actual the actual value in the failed assertion.
   * @param keys the unexpected keys
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <K> ErrorMessageFactory shouldNotContainKeys(Object actual, Set<K> keys) {
    if (keys.size() == 1) return shouldNotContainKey(actual, keys.iterator().next());
    return new ShouldNotContainKeys(actual, keys);
  }

  private <K> ShouldNotContainKeys(Object actual, Set<K> key) {
    super("%nExpecting:%n  <%s>%nnot to contain keys:%n  <%s>", actual, key);
  }
}
