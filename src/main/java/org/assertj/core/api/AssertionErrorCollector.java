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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.util.List;

public interface AssertionErrorCollector extends AfterAssertionErrorCollected {

  /**
   * This method can be used to collect soft assertion errors.
   * <p>
   * <b>Warning:</b> this is not the method used internally by AssertJ to collect all of them, overriding it to react to each
   * collected assertion error will not work.
   * <p>
   * To be able to react after an assertion error is collected, use @{@link #onAssertionErrorCollected(AssertionError)} instead.
   *
   * @param error the {@link AssertionError} to collect.
   */
  void collectAssertionError(AssertionError error);

  List<AssertionError> assertionErrorsCollected();

  @Override
  default void onAssertionErrorCollected(AssertionError assertionError) {
    // nothing by default
  }
}
