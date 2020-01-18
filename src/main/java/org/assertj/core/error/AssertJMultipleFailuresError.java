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

import static java.lang.String.format;

import java.util.List;

import org.opentest4j.MultipleFailuresError;

/**
 * AssertJ version of {@link MultipleFailuresError} to get more control on the error formatting.
 */
public class AssertJMultipleFailuresError extends MultipleFailuresError {
  private static final long serialVersionUID = 1L;
  private static final String EOL = System.getProperty("line.separator");
  private static final String ERROR_SEPARATOR = EOL + "-- failure %d --";
  private String heading;

  public AssertJMultipleFailuresError(String heading, List<? extends Throwable> failures) {
    super(heading, failures);
    this.heading = heading;
  }

  @Override
  public String getMessage() {

    List<Throwable> failures = getFailures();
    int failureCount = failures.size();

    if (failureCount == 0) return super.getMessage();

    heading = isBlank(heading) ? "Multiple Failures" : heading.trim();
    StringBuilder builder = new StringBuilder(EOL).append(heading)
                                                  .append(" (")
                                                  .append(failureCount).append(" ")
                                                  .append(pluralize(failureCount, "failure", "failures"))
                                                  .append(")");
    for (int i = 0; i < failureCount; i++) {
      builder.append(errorSeparator(i + 1));
      String message = nullSafeMessage(failures.get(i));
      // when we have a description, we add a line before for readability
      if (hasDescription(message)) builder.append(EOL);
      builder.append(message);
    }

    return builder.toString();
  }

  private String errorSeparator(int errorNumber) {
    return format(ERROR_SEPARATOR, errorNumber);
  }

  private boolean hasDescription(String message) {
    return message.startsWith("[");
  }

  private static boolean isBlank(String str) {
    return str == null || str.trim().length() == 0;
  }

  private static String pluralize(int count, String singular, String plural) {
    return count == 1 ? singular : plural;
  }

  private static String nullSafeMessage(Throwable failure) {
    return isBlank(failure.getMessage()) ? "<no message> in " + failure.getClass().getName() : failure.getMessage();
  }

}
