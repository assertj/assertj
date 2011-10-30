/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeSorted.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;

import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;

/**
 * Tests for <code>{@link ObjectArrays#assertIsSorted(AssertionInfo, Object[])}</code>.
 * 
 * @author Joel Costigliola
 */
public class ObjectArrays_assertIsSortedAccordingToComparator_Test {

  @Rule
  public ExpectedException thrown = none();

  private Failures failures;
  private Object[] actual;
  private ObjectArrays arrays;
  private Comparator<String> stringDescendingOrderComparator;
  private Comparator<Object> comparator;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    actual = array("Yoda", "Vador", "Luke", "Luke", "Leia");
    arrays = new ObjectArrays();
    arrays.failures = failures;
    stringDescendingOrderComparator = new Comparator<String>() {
      public int compare(String s1, String s2) {
        return -s1.compareTo(s2);
      }
    };
    comparator = new Comparator<Object>() {
      public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
      }
    };
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, stringDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), new String[0], stringDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), new String[0], comparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSortedAccordingToComparator(someInfo(), null, comparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expect(NullPointerException.class);
    arrays.assertIsSortedAccordingToComparator(someInfo(), array(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = array("Yoda", "Vador", "Leia", "Leia", "Luke");
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(3, actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_not_mutually_comparable_elements_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = array("bar", new Object(), "foo");
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveComparableElementsAccordingToGivenComparator(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_unique_element_is_not_compatible_with_given_comparator() {
    AssertionInfo info = someInfo();
    actual = array(new Object());
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveComparableElementsAccordingToGivenComparator(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_elements_are_not_compatible_with_given_comparator() {
    AssertionInfo info = someInfo();
    actual = array(new Integer(1), new Integer(2));
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveComparableElementsAccordingToGivenComparator(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
}
