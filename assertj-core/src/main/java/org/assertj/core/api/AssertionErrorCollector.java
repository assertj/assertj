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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import java.util.List;
import java.util.Optional;

public interface AssertionErrorCollector extends AfterAssertionErrorCollected {

  /**
   * Optionally sets a "delegate" collector into which the collected assertions will be deposited.
   * <p>
   * Note that if you set a delegate, this instance will no longer collect or report assertion errors itself but will
   * forward them all to the delegate for collection.
   *
   * @param delegate the {@link AssertionErrorCollector} to which the assertions will be forwarded.
   */
  default void setDelegate(AssertionErrorCollector delegate) {}

  default Optional<AssertionErrorCollector> getDelegate() {
    return Optional.empty();
  }

  /**
   * This method can be used to collect soft assertion errors.
   * <p>
   * To be able to react after an assertion error is collected, use {@link #onAssertionErrorCollected(AssertionError)}.
   *
   * @param error the {@link AssertionError} to collect.
   */
  void collectAssertionError(AssertionError error);

  List<AssertionError> assertionErrorsCollected();

  @Override
  default void onAssertionErrorCollected(AssertionError assertionError) {
    // nothing by default
  }

  /**
   * Indicates/sets that the last assertion was a success.
   */
  void succeeded();

  /**
   * Returns the result of last soft assertion which can be used to decide what the next one should be.
   * <p>
   * Example:
   * <pre><code class='java'> Person person = ...
   * SoftAssertions soft = new SoftAssertions();
   * if (soft.assertThat(person.getAddress()).isNotNull().wasSuccess()) {
   *     soft.assertThat(person.getAddress().getStreet()).isNotNull();
   * }</code></pre>
   *
   * @return true if the last assertion was a success.
   */
  boolean wasSuccess();
}
