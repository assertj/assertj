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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.error.ZippedElementsShouldSatisfy.zippedElementsShouldSatisfy;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.error.ZippedElementsShouldSatisfy.ZipSatisfyError;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

public class Iterables_assertZipSatisfy_Test extends IterablesBaseTest {

  private List<String> other = newArrayList("LUKE", "YODA", "LEIA");

  @Test
  public void should_satisfy_single_zip_requirement() {
    iterables.assertZipSatisfy(someInfo(), actual, other, (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
  }

  @Test
  public void should_satisfy_compound_zip_requirements() {
    iterables.assertZipSatisfy(someInfo(), actual, other, (s1, s2) -> {
      assertThat(s1).isEqualToIgnoringCase(s2);
      assertThat(s1).startsWith(firstChar(s2));
    });
  }

  @Test
  public void should_pass_if_both_iterables_are_empty() {
    actual.clear();
    other.clear();
    iterables.assertZipSatisfy(someInfo(), actual, other, (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
  }

  @Test
  public void should_fail_according_to_requirements() {
    // GIVEN
    ThrowingCallable assertion = () -> iterables.assertZipSatisfy(someInfo(), actual, other,
                                                                  (s1, s2) -> assertThat(s1).startsWith(s2));
    // WHEN
    AssertionError assertionError = catchThrowableOfType(assertion, AssertionError.class);
    // THEN
    assertThat(assertionError).isNotNull();
    List<ZipSatisfyError> errors = list(new ZipSatisfyError("Luke", "LUKE", shouldStartWith("Luke", "LUKE").create()),
                                        new ZipSatisfyError("Yoda", "YODA", shouldStartWith("Yoda", "YODA").create()),
                                        new ZipSatisfyError("Leia", "LEIA", shouldStartWith("Leia", "LEIA").create()));
    verify(failures).failure(info, zippedElementsShouldSatisfy(actual, other, errors));
  }

  @Test
  public void should_fail_when_compared_iterables_have_different_sizes() {
    other.add("Vader");
    try {
      iterables.assertZipSatisfy(someInfo(), actual, other, (s1, s2) -> assertThat(s1).startsWith(s2));
    } catch (AssertionError e) {
      assertThat(e).hasMessageContaining(shouldHaveSameSizeAs(actual, actual.size(), other.size()).create());
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_consumer_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).zipSatisfy(other, null))
                                    .withMessage("The BiConsumer expressing the assertions requirements must not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->{
      actual = null;
      iterables.assertZipSatisfy(someInfo(), actual, other, (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_other_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      other = null;
      iterables.assertZipSatisfy(someInfo(), actual, other, (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
    }).withMessage("The iterable to zip actual with must not be null");
  }

  private static String firstChar(String s2) {
    return String.valueOf(s2.charAt(0));
  }

}
