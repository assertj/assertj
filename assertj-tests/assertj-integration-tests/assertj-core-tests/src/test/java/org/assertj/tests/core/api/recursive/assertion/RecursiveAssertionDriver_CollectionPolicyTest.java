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
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.CollectionAssertionPolicy.COLLECTION_OBJECT_AND_ELEMENTS;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.CollectionAssertionPolicy.COLLECTION_OBJECT_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.CollectionAssertionPolicy.ELEMENTS_ONLY;
import static org.assertj.core.util.Lists.list;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_CollectionPolicyTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  void should_assert_over_collection_object_but_not_elements_when_policy_is_collection_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withCollectionAssertionPolicy(COLLECTION_OBJECT_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = testObjectWithACollection();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("collection"));
  }

  @Test
  void should_assert_over_array_object_but_not_elements_when_policy_is_collection_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withCollectionAssertionPolicy(COLLECTION_OBJECT_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = testObjectWithAnArray();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("array"));
  }

  @Test
  void should_assert_over_collection_elements_but_not_collection_when_policy_is_elements_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withCollectionAssertionPolicy(ELEMENTS_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = testObjectWithACollection();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("collection").field("[0]"),
                                    rootFieldLocation().field("collection").field("[1]"));
  }

  @Test
  void should_assert_over_array_elements_but_not_object_when_policy_is_collection_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withCollectionAssertionPolicy(ELEMENTS_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = testObjectWithAnArray();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("array").field("[0]"),
                                    rootFieldLocation().field("array").field("[1]"));
  }

  @Test
  void should_assert_over_collection_object_and_elements_when_policy_is_object_and_elements() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withCollectionAssertionPolicy(COLLECTION_OBJECT_AND_ELEMENTS)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = testObjectWithACollection();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("collection"),
                                    rootFieldLocation().field("collection").field("[0]"),
                                    rootFieldLocation().field("collection").field("[1]"));
  }

  @Test
  void should_assert_over_array_object_and_elements_when_policy_is_object_and_elements() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withCollectionAssertionPolicy(COLLECTION_OBJECT_AND_ELEMENTS)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = testObjectWithAnArray();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("array"),
                                    rootFieldLocation().field("array").field("[0]"),
                                    rootFieldLocation().field("array").field("[1]"));
  }

  private Object testObjectWithACollection() {
    ClassWithCollectionChild child = new ClassWithCollectionChild();
    child.collection.add("Hello World!");
    child.collection.add("Goodbye World!");
    return child;
  }

  private Object testObjectWithAnArray() {
    ClassWithArrayChild child = new ClassWithArrayChild();
    child.array[0] = "Hello World!";
    child.array[1] = "Goodbye World!";
    return child;
  }

  class ClassWithCollectionChild {
    Collection<Object> collection = list();
  }

  class ClassWithArrayChild {
    Object[] array = new Object[2];
  }
}
