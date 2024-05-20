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

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_JavaClassLibraryRecursionTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  void should_assert_over_but_not_recurse_into_jcl_classes_when_configured_not_to_recurse_into_JCL() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object testObject = jclReferencingObject();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("jclField"));
  }

  @Test
  void should_assert_over_and_recurse_into_jcl_classes_when_configured_to_recurse_into_JCL() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withRecursionIntoJavaClassLibraryTypes(true)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = jclReferencingObject();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSizeGreaterThanOrEqualTo(3)
                      .contains(rootFieldLocation().field("jclField"),
                                // OOOOH, knowledge of String internals! this is why recursion into JCL types is off by default
                                rootFieldLocation().field("jclField").field("coder"));
  }

  private Object jclReferencingObject() {
    return new ClassWithAFieldWithATypeFromTheJCL();
  }

  class ClassWithAFieldWithATypeFromTheJCL {
    @SuppressWarnings("unused")
    private String jclField = "Hello World";
  }

}
