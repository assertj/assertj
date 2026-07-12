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

import java.io.Serial;
import java.util.List;

import org.assertj.core.description.Description;
import org.assertj.core.presentation.StandardRepresentation;

/** Error aggregating multiple assertion failures. */
public class MultipleAssertionsError extends AssertionError {

  @Serial
  private static final long serialVersionUID = -5547434453993413952L;

  /** The assertion description. */
  private final Description description;
  /** The object under test. */
  private final Object objectUnderTest;
  /** The aggregated assertion failures. */
  private final List<AssertionError> errors;
  /** The aggregated error message. */
  private final String message;

  /**
   * Creates an aggregated assertion error.
   *
   * @param description the assertion description
   * @param objectUnderTest the object under test
   * @param errors the assertion failures
   */
  public MultipleAssertionsError(Description description, Object objectUnderTest, List<AssertionError> errors) {
    this.description = description;
    this.objectUnderTest = objectUnderTest;
    this.errors = errors;
    message = StandardRepresentation.STANDARD_REPRESENTATION.toStringOf(this);
  }

  @Override
  public String getMessage() {
    return message;
  }

  /**
   * Returns the object under test.
   *
   * @return the object under test
   */
  public Object getObjectUnderTest() {
    return objectUnderTest;
  }

  /**
   * Returns the assertion description.
   *
   * @return the assertion description
   */
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
