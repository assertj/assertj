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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.doublepredicate;

import java.util.function.DoublePredicate;
import java.util.function.Predicate;

import org.assertj.core.api.DoublePredicateAssert;
import org.assertj.core.api.DoublePredicateAssertBaseTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * @author Filip Hrisafov
 */
public class DoublePredicateAssert_acceptsAll_Test extends DoublePredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());
    double[] acceptedValues = new double[] { 1, 2 };

    assertThat((DoublePredicate) null).acceptsAll(acceptedValues);
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_all_values() {
    DoublePredicate predicate = val -> val <= 2;
    Predicate<Double> wrapPredicate = predicate::test;
    double[] matchValues = new double[] { 1, 2, 3 };
    thrown.expectAssertionError(elementsShouldMatch(matchValues, 3D, wrapPredicate).create());
    assertThat(predicate).acceptsAll(matchValues);
  }

  @Test
  public void should_pass_when_predicate_accepts_all_values() {
    DoublePredicate predicate = val -> val <= 2;
    assertThat(predicate).acceptsAll(1, 2);
  }

  @Override
  protected DoublePredicateAssert invoke_api_method() {
    return assertions.acceptsAll(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(1D, 2D), wrapped);
  }
}
