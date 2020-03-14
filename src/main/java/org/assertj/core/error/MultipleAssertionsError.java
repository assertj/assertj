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
package org.assertj.core.error;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.AssertionErrorMessagesAggregrator.aggregrateErrorMessages;

import java.util.List;

import org.assertj.core.description.Description;

public class MultipleAssertionsError extends AssertionError {

  private static final long serialVersionUID = -5547434453993413952L;
  private final List<? extends AssertionError> errors;

  public MultipleAssertionsError(List<? extends AssertionError> errors) {
    super(createMessage(errors));
    this.errors = errors;
  }

  public MultipleAssertionsError(Description description, List<? extends AssertionError> errors) {
    super(formatDescription(description) + createMessage(errors));
    this.errors = errors;
  }

  /**
   * Returns the causal AssertionErrors in the order that they were thrown.
   * 
   * @return the list of errors
   */
  public List<? extends AssertionError> getErrors() {
    return errors;
  }

  private static String formatDescription(Description description) {
    return DescriptionFormatter.instance().format(description);
  }

  private static String createMessage(List<? extends AssertionError> errors) {
    List<String> errorsMessage = errors.stream()
                                       .map(AssertionError::getMessage)
                                       .collect(toList());
    return aggregrateErrorMessages(errorsMessage);
  }

}
