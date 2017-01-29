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
package org.assertj.core.api.longpredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.api.LongPredicateAssert;
import org.assertj.core.api.LongPredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class LongPredicateAssert_accepts_Test extends LongPredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((LongPredicate) null).accepts(1L, 2L);
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_value() {
    LongPredicate predicate = val -> val <= 2;
    Predicate<Long> wrapPredicate = predicate::test;
    long expectedValue = 3;
    thrown.expectAssertionError(shouldAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());

    assertThat(predicate).accepts(expectedValue);
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_value_with_string_description() {
    LongPredicate predicate = val -> val <= 2;
    Predicate<Long> wrapPredicate = predicate::test;
    long expectedValue = 3;
    thrown.expectAssertionError("[test] " + shouldAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());

    assertThat(predicate).as("test").accepts(expectedValue);
  }

  @Test
  public void should_pass_when_predicate_accepts_value() {
    LongPredicate predicate = val -> val <= 2;

    assertThat(predicate).accepts(1);
  }


  @Test
  public void should_fail_when_predicate_does_not_accept_values() {
    LongPredicate predicate = val -> val <= 2;
    long[] matchValues = new long[] { 1L, 2L, 3L };
    thrown.expectAssertionError(elementsShouldMatch(matchValues, 3L, PredicateDescription.GIVEN).create());

    assertThat(predicate).accepts(matchValues);
  }

  @Test
  public void should_pass_when_predicate_accepts_all_values() {
    LongPredicate predicate = val -> val <= 2;

    assertThat(predicate).accepts(1L, 2L);
  }

  @Override
  protected LongPredicateAssert invoke_api_method() {
    return assertions.accepts(1L, 2L);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(1L, 2L), wrapped, PredicateDescription.GIVEN);
  }
}
