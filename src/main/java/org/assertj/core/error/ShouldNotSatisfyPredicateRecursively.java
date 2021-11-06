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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.api.recursive.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;

import java.util.List;

public class ShouldNotSatisfyPredicateRecursively extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldNotSatisfyRecursively(RecursiveAssertionConfiguration recursiveassertionConfiguration,
                                                                List<FieldLocation> failedFields) {
    StringBuilder builder = new StringBuilder("\n")
      .append("The following fields did not satisfy the predicate: \n");
    failedFields.stream()
      .map(FieldLocation::getPathToUseInErrorReport)
      .map(ShouldNotSatisfyPredicateRecursively::toReportLine)
      .forEach(builder::append);
    builder.append("The recursive assertion was performed with this configuration:\n");
    builder.append(recursiveassertionConfiguration.toString());
    return new ShouldNotSatisfyPredicateRecursively(builder.toString());
  }

  private static String toReportLine(String s) {
    return "  " + s + "\n";
  }

  private ShouldNotSatisfyPredicateRecursively(String message, Object... arguments) {
    super(message, arguments);
  }

}
