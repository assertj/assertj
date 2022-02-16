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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.error;

import java.util.Set;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Creates an error message indicating that an assertion that verifies a map contains some keys failed. TODO : move to
 * assertj-core to replace {@link org.assertj.core.error.ShouldContainKeys}.
 *
 * @author Joel Costigliola
 */
public class ShouldContainKeys extends BasicErrorMessageFactory {

  private ShouldContainKeys(Object actual, Object key) {
    super("%nExpecting:%n  %s%nto contain key:%n  %s", actual, key);
  }

  private ShouldContainKeys(Object actual, Object[] keys, Set<?> keysNotFound) {
    super("%nExpecting:%n  %s%nto contain keys:%n  %s%nbut could not find:%n  %s", actual, keys, keysNotFound);
  }

  /**
   * Creates a new <code>{@link ShouldContainKeys}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param keys the expected keys.
   * @param keysNotFound the missing keys.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainKeys(Object actual, Object[] keys, Set<?> keysNotFound) {
    return keys.length == 1 ? new ShouldContainKeys(actual, keys[0]) : new ShouldContainKeys(actual, keys, keysNotFound);
  }
}
