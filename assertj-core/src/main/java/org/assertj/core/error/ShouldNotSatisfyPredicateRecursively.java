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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.FieldLocation;

public class ShouldNotSatisfyPredicateRecursively extends BasicErrorMessageFactory {

  private static final String INDENT = "  ";
  private static final String NEW_LINE = format("%n");

  public static ErrorMessageFactory shouldNotSatisfyRecursively(RecursiveAssertionConfiguration recursiveAssertionConfiguration,
                                                                List<FieldLocation> failedFields) {
    List<String> fieldsDescription = failedFields.stream().map(FieldLocation::getPathToUseInErrorReport).collect(toList());
    StringBuilder builder = new StringBuilder(NEW_LINE);
    builder.append("The following fields did not satisfy the predicate:").append(NEW_LINE);
    builder.append(INDENT + fieldsDescription.toString() + NEW_LINE);
    builder.append("The recursive assertion was performed with this configuration:").append(NEW_LINE);
    builder.append(recursiveAssertionConfiguration);
    return new ShouldNotSatisfyPredicateRecursively(builder.toString());
  }

  private ShouldNotSatisfyPredicateRecursively(String message) {
    super(message);
  }

}
