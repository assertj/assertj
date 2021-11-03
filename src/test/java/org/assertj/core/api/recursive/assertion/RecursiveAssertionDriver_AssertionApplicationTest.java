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
package org.assertj.core.api.recursive.assertion;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.recursive.FieldLocation;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_AssertionApplicationTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  void should_call_predicate_for_the_root_object() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object emptyTestObject = emptyTestObject();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(succeedingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).isEmpty();
    verify(succeedingMockPredicate, times(1)).test(eq(emptyTestObject));
  }

  @Test
  void should_mark_the_root_object_as_failed_when_it_fails_the_predicate() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object emptyTestObject = emptyTestObject();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).singleElement().hasFieldOrPropertyWithValue("getPathToUseInRules", "");
    verify(failingMockPredicate, times(1)).test(eq(emptyTestObject));
  }

  @Test
  void should_assert_over_null_when_configured_to_do_so() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object emptyTestObject = objectWithNullField();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).hasSize(2).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("nullField"));
  }

  @Test
  void should_not_assert_over_null_when_configured_not_to_do_so() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder().withIgnoreAllActualNullFields(true)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object emptyTestObject = objectWithNullField();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, emptyTestObject);
    // THEN
    then(failedFields).hasSize(1).contains(FieldLocation.rootFieldLocation());
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
    then(failedFields).hasSize(2).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("linkToMiddle"));
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
    then(failedFields).hasSize(2).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("linkToMiddle"));
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
    then(failedFields).hasSize(2).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("linkToMiddle"));
  }

  @Test
  void should_not_assert_over_empty_optionals_if_configured_to_ignore_them() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
      .withIgnoreAllActualEmptyOptionalFields(true)
      .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = Optional.empty();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).isEmpty();
  }

  @Test
  void should_assert_over_empty_optionals_if_configured_to_include_them() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = Optional.empty();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(1).contains(FieldLocation.rootFieldLocation());
  }

}
