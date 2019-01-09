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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Strings.escapePercent;

import java.util.List;

public class ZippedElementsShouldSatisfy extends BasicErrorMessageFactory {

  private static final String DELIMITER = String.format("%n%n- ");

  public static ErrorMessageFactory zippedElementsShouldSatisfy(Object actual, Object other,
                                                                List<ZipSatisfyError> zipSatisfyErrors) {
    return new ZippedElementsShouldSatisfy(actual, other, zipSatisfyErrors);
  }

  private ZippedElementsShouldSatisfy(Object actual, Object other, List<ZipSatisfyError> zipSatisfyErrors) {
    // no use of %s for describe(zipSatisfyErrors) to avoid extra ""
    super("%n" +
          "Expecting zipped elements of:%n" +
          "  <%s>%n" +
          "and:%n" +
          "  <%s>%n" +
          "to satisfy given requirements but these zipped elements did not:" + describe(zipSatisfyErrors),
          actual, other);
  }

  private static String describe(List<ZipSatisfyError> zipSatisfyErrors) {
    List<String> errorsToStrings = zipSatisfyErrors.stream()
                                                   .map(ZipSatisfyError::toString)
                                                   .collect(toList());
    return escapePercent(DELIMITER + String.join(DELIMITER, errorsToStrings));
  }

  public static class ZipSatisfyError {
    public final Object actualElement;
    public final Object otherElement;
    public final String error;

    public ZipSatisfyError(Object actualElement, Object otherElement, String error) {
      this.actualElement = actualElement;
      this.otherElement = otherElement;
      this.error = error;
    }

    @Override
    public String toString() {
      return String.format("(%s, %s) error: %s", actualElement, otherElement, error);
    }

  }

}
