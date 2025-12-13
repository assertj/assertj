/*
 * Copyright 2012-2025 the original author or authors.
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

import java.io.Serial;
import java.util.List;

import org.assertj.core.description.Description;
import org.assertj.core.presentation.StandardRepresentation;

public class MultipleAssertionsError extends AssertionError {

  @Serial
  private static final long serialVersionUID = -5547434453993413952L;

  private final Description description;
  private final Object objectUnderTest;
  private final List<AssertionError> errors;

  public MultipleAssertionsError(Description description, Object objectUnderTest, List<AssertionError> errors) {
    this.description = description;
    this.objectUnderTest = objectUnderTest;
    this.errors = errors;
  }

  @Override
  public String getMessage() {
    return StandardRepresentation.STANDARD_REPRESENTATION.toStringOf(this);
  }

  public Object getObjectUnderTest() {
    return objectUnderTest;
  }

  public Description getDescription() {
    return description;
  }

  /**
   * Returns the causal AssertionErrors in the order that they were thrown.
   *
   * @return the list of errors
   */
  public List<AssertionError> getErrors() {
    return errors;
  }
}
