/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.assertion;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_AssertionApplicationTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  void should_not_call_predicate_for_the_root_object() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object emptyTestObject = emptyTestObject();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(succeedingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).isEmpty();
    verifyNoInteractions(succeedingMockPredicate);
  }

  @Test
  void should_assert_over_null_when_configured_to_do_so() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object emptyTestObject = objectWithNullField();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("nullField"));
  }

  @Test
  void should_not_assert_over_null_when_configured_not_to_do_so() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder().withIgnoreAllNullFields(true)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object emptyTestObject = objectWithNullField();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).isEmpty();
  }

  @Test
  void should_not_assert_over_fields_that_are_being_ignored_by_name() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withIgnoredFields("linkToMiddle.linkToBottom")
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = simpleCycleStructure();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("linkToMiddle"));
  }

  @Test
  void should_not_assert_over_fields_that_are_being_ignored_by_regex() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withIgnoredFieldsMatchingRegexes("linkToMiddle\\.link.*")
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = simpleCycleStructure();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("linkToMiddle"));
  }

  @Test
  void should_not_assert_over_fields_that_are_being_ignored_by_type() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withIgnoredFieldsOfTypes(Bottom.class)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = simpleCycleStructure();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("linkToMiddle"));
  }

  @Test
  void should_assert_over_empty_optionals() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = Optional.empty();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).isEmpty();
  }

}
