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

import org.assertj.core.api.BaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * @author Filip Hrisafov
 */
public class DoublePredicateAssert_accepts_Test extends BaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((DoublePredicate) null).accepts(1);
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_value() {
    DoublePredicate predicate = val -> val <= 2;
    Predicate<Double> wrapPredicate = predicate::test;
    double expectedValue = 3;
    thrown.expectAssertionError(shouldMatch(expectedValue, wrapPredicate, PredicateDescription.GIVEN).create());
    assertThat(predicate).accepts(expectedValue);
  }

  @Test
  public void should_pass_when_predicate_accepts_value() {
    DoublePredicate predicate = val -> val <= 2;
    assertThat(predicate).accepts(1);
  }
}
