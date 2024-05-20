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

import java.util.List;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_PrimitiveFieldHandlingTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  void should_assert_over_primitive_field_when_configured_to_do_so() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object objectToAssertOver = objectToAssertOver();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, objectToAssertOver);
    // THEN
    then(failedFields).hasSize(2).contains(rootFieldLocation().field("primitiveField"));
  }

  @Test
  void should_not_assert_over_primitive_field_when_configured_to_ignore_them() {
    // GIVEN
    RecursiveAssertionConfiguration recursiveAssertionConfiguration = RecursiveAssertionConfiguration.builder()
                                                                                                     .withIgnorePrimitiveFields(true)
                                                                                                     .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(recursiveAssertionConfiguration);
    Object objectToAssertOver = objectToAssertOver();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, objectToAssertOver);
    // THEN
    then(failedFields).doesNotContain(rootFieldLocation().field("primitiveField"));
  }

  @Test
  void should_assert_over_inherited_primitive_field_when_configured_to_do_so() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object objectToAssertOver = objectHierarchyToAssertOver();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, objectToAssertOver);
    // THEN
    then(failedFields).contains(rootFieldLocation().field("primitiveField"),
                                rootFieldLocation().field("objectField"),
                                rootFieldLocation().field("anotherObjectField"));
  }

  private Object objectToAssertOver() {
    return new ClassWithPrimitiveAndObjectField();
  }

  private Object objectHierarchyToAssertOver() {
    return new SubClassWithAdditionalField();
  }

  @SuppressWarnings("unused")
  private class ClassWithPrimitiveAndObjectField {
    private int primitiveField = 0;
    private Object objectField = new Object();
  }

  @SuppressWarnings("unused")
  private class SubClassWithAdditionalField extends ClassWithPrimitiveAndObjectField {
    private Object anotherObjectField = new Object();
  }

}
