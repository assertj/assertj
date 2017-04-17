/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertDoesNotHaveDuplicates(AssertionInfo, Collection)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Iterables_assertDoesNotHaveDuplicates_Test extends IterablesBaseTest {

  private static final int GENERATED_OBJECTS_NUMBER = 50000;
  private final List<String> actual = newArrayList("Luke", "Yoda", "Leia");

  @Test
  public void should_pass_if_actual_does_not_have_duplicates() {
    iterables.assertDoesNotHaveDuplicates(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    iterables.assertDoesNotHaveDuplicates(someInfo(), emptyList());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertDoesNotHaveDuplicates(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates() {
    AssertionInfo info = someInfo();
    Collection<String> duplicates = newLinkedHashSet("Luke", "Yoda");
    actual.addAll(duplicates);
    try {
      iterables.assertDoesNotHaveDuplicates(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotHaveDuplicates(actual, duplicates));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_within_time_constraints() {
    List<String> generated = new ArrayList<>(GENERATED_OBJECTS_NUMBER);
    for (int count = 0; count < GENERATED_OBJECTS_NUMBER; count++) {
      generated.add(UUID.randomUUID().toString());
    }

    long time = System.currentTimeMillis();
    iterables.assertDoesNotHaveDuplicates(someInfo(), generated);
    // check that it takes less than 2 seconds, usually it takes 100ms on an average computer
    // with the previous implementation, it would take minutes ...
    System.out.println("Time elapsed in ms for assertDoesNotHaveDuplicates : " + (System.currentTimeMillis() - time));
    assertThat((System.currentTimeMillis() - time)).isLessThan(2000);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_array() {
    Collection<String[]> actual = newArrayList(array("Luke", "Yoda"), array("Luke", "Yoda"));
    // duplicates is commented, because mockito is not smart enough to compare arrays contents 
    // Collection<String[]> duplicates = newLinkedHashSet();
    // duplicates.add(array("Luke", "Yoda"));
    try {
      iterables.assertDoesNotHaveDuplicates(someInfo(), actual);
    } catch (AssertionError e) {
      // can't use verify since mockito not smart enough to compare arrays contents
      // verify(failures).failure(info, shouldNotHaveDuplicates(actual, duplicates));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_does_not_have_duplicates_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotHaveDuplicates(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Collection<String> duplicates = newLinkedHashSet("LUKE", "yoda");
    actual.addAll(duplicates);
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotHaveDuplicates(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotHaveDuplicates(actual, duplicates, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void should_pass_within_time_constraints_with_custom_comparison_strategy() {
    List<String> generated = new ArrayList<>(GENERATED_OBJECTS_NUMBER);
    for (int count = 0; count < GENERATED_OBJECTS_NUMBER; count++) {
      generated.add(UUID.randomUUID().toString());
    }

    long time = System.currentTimeMillis();
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotHaveDuplicates(someInfo(), generated);
    // check that it takes less than 10 seconds, usually it takes 1000ms on an average computer
    // with the previous implementation, it would take minutes ...
    System.out.println("Time elapsed in ms for assertDoesNotHaveDuplicates with custom comparison strategy : " + (System.currentTimeMillis() - time));
    assertThat((System.currentTimeMillis() - time)).isLessThan(10000);
  }

}
