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
