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

import org.assertj.core.api.recursive.FieldLocation;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;

public class RecursiveAssertionDriver_OptionalHandlingTest extends AbstractRecursiveAssertionDriverTestBase{
  @Test
  void should_assert_value_of_optional_even_when_not_recursing_into_JCL_types() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = objectWithSomeSortOfOptional(Optional.of(new Object()));
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(3).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("optional"),
                                           FieldLocation.rootFieldLocation().field("optional").field("VAL")
                                           );
  }

  @Test
  void should_assert_value_of_optional_int_even_when_not_recursing_into_JCL_types() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = objectWithSomeSortOfOptional(OptionalInt.of(42));
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(3).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("optional"),
                                           FieldLocation.rootFieldLocation().field("optional").field("VAL")
                                           );
  }

  @Test
  void should_assert_value_of_optional_double_even_when_not_recursing_into_JCL_types() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = objectWithSomeSortOfOptional(OptionalDouble.of(0.0123));
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(3).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("optional"),
                                           FieldLocation.rootFieldLocation().field("optional").field("VAL")
                                           );
  }@Test
  void should_assert_value_of_optional_long_even_when_not_recursing_into_JCL_types() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = objectWithSomeSortOfOptional(OptionalLong.of(1223786342436215340L));
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(3).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("optional"),
                                           FieldLocation.rootFieldLocation().field("optional").field("VAL")
                                           );
  }

  @Test
  void should_recurse_into_empty_optional_even_when_not_recursing_into_JCL_types_and_not_fail() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = objectWithSomeSortOfOptional(Optional.empty());
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(3).contains(FieldLocation.rootFieldLocation(),
                                           FieldLocation.rootFieldLocation().field("optional"),
                                           FieldLocation.rootFieldLocation().field("optional").field("VAL"));
  }

  private Object objectWithSomeSortOfOptional(Object opt) {
    ClassWithOptional cwo = new ClassWithOptional();
    cwo.optional = opt;
    return cwo;
  }

  class ClassWithOptional {
    private Object optional;
  }
}
