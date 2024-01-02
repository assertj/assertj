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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotSatisfyPredicateRecursively.shouldNotSatisfyRecursively;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ShouldNotSatisfyPredicateRecursively_create_Test {

  private static final TextDescription DESCRIPTION = new TextDescription("Test");

  @Test
  void should_create_error_message() {
    // GIVEN
    RecursiveAssertionConfiguration recursiveAssertionConfiguration = RecursiveAssertionConfiguration.builder().build();
    List<FieldLocation> failedFields = list(new FieldLocation("name"), new FieldLocation("address"));
    ErrorMessageFactory factory = shouldNotSatisfyRecursively(recursiveAssertionConfiguration, failedFields);
    // WHEN
    String message = factory.create(DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "The following fields did not satisfy the predicate:%n" +
                                   "  [name, address]%n" +
                                   "The recursive assertion was performed with this configuration:%n%s",
                                   recursiveAssertionConfiguration));
  }

}
