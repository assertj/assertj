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
package org.assertj.core.api;

import java.util.List;
import java.util.Optional;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.soft.SoftObjectAssert;
import org.assertj.core.api.soft.SoftOptionalAssert;
import org.assertj.core.error.AssertionErrorCreator;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class GeneratedSoftAssertions {

  private static final AssertionErrorCreator ASSERTION_ERROR_CREATOR = new AssertionErrorCreator();
  private final AssertionErrorCollector assertionErrorCollector = new DefaultAssertionErrorCollector();

  /**
   * Create assertion for {@link Optional}.
   *
   * @param actual  the actual value.
   * @param <VALUE> the type of the value contained in the {@link Optional}.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public <VALUE> SoftOptionalAssert<VALUE> assertThat(Optional<VALUE> actual) {
    return new SoftOptionalAssert<>(actual, assertionErrorCollector);
  }

  /**
   * Create assertion for {@link Optional}.
   *
   * @param actual  the actual value.
   * @param <ACTUAL> the type of the value contained in the {@link Optional}.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public <ACTUAL> SoftObjectAssert<ACTUAL> assertThat(ACTUAL actual) {
    return new SoftObjectAssert<>(actual, assertionErrorCollector);
  }

  public List<AssertionError> errorsCollected() {
    return assertionErrorCollector.assertionErrorsCollected();
  }

  public void assertAll() {
    List<AssertionError> errors = errorsCollected();
    if (!errors.isEmpty()) throw ASSERTION_ERROR_CREATOR.multipleAssertionsError(errors);

  }

  public AssertionErrorCollector getAssertionErrorCollector() {
    return assertionErrorCollector;
  }
}
