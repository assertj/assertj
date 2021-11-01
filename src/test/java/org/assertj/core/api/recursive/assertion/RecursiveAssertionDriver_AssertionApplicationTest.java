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

}
