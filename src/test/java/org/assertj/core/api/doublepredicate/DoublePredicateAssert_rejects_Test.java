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
package org.assertj.core.api.doublepredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.assertj.core.api.DoublePredicateAssert;
import org.assertj.core.api.DoublePredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class DoublePredicateAssert_rejects_Test extends DoublePredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((DoublePredicate) null).rejects(1.0, 2.0, 3.0);
  }
  @Test
  public void should_pass_when_predicate_does_not_accept_value() {
    DoublePredicate predicate = val -> val <= 2;

    assertThat(predicate).rejects(3.0);
  }

  @Test
  public void should_fail_when_predicate_accepts_value() {
    DoublePredicate predicate = val -> val <= 2;
    Predicate<Double> wrapPredicate = predicate::test;
    double expectedValue = 2.0;
    thrown.expectAssertionError(shouldNotAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());

    assertThat(predicate).rejects(expectedValue);
  }

  @Test
  public void should_fail_when_predicate_accepts_value_with_string_description() {
    DoublePredicate predicate = val -> val <= 2;
    Predicate<Double> wrapPredicate = predicate::test;
    double expectedValue = 2.0;
    thrown.expectAssertionError("[test] " + shouldNotAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());

    assertThat(predicate).as("test").rejects(expectedValue);
  }

  @Test
  public void should_fail_when_predicate_accepts_some_value() {
    DoublePredicate predicate = num -> num <= 2;
    double[] matchValues = new double[] { 1.0, 2.0, 3.0 };
    List<Double> matchValuesList = DoubleStream.of(matchValues).boxed().collect(Collectors.toList());
    thrown.expectAssertionError(noElementsShouldMatch(matchValuesList, 1D, PredicateDescription.GIVEN).create());
    assertThat(predicate).rejects(matchValues);
  }

  @Test
  public void should_pass_when_predicate_accepts_no_value() {
    DoublePredicate predicate = num -> num <= 2;

    assertThat(predicate).rejects(3.0, 4.0, 5.0);
  }

  @Override
  protected DoublePredicateAssert invoke_api_method() {
    return assertions.rejects(3.0, 4.0);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertNoneMatch(getInfo(assertions), newArrayList(3.0D, 4.0D), wrapped,
                                      PredicateDescription.GIVEN);
  }
}
