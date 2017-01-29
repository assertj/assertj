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
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.function.DoublePredicate;

import org.assertj.core.api.DoublePredicateAssert;
import org.assertj.core.api.DoublePredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class DoublePredicateAssert_accepts_Test extends DoublePredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((DoublePredicate) null).accepts(1.0, 2.0, 3.0);
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_all_values() {
    DoublePredicate predicate = val -> val <= 2;
    double[] matchValues = new double[] { 1.0, 2.0, 3.0 };
    thrown.expectAssertionError(elementsShouldMatch(matchValues, 3D, PredicateDescription.GIVEN).create());

    assertThat(predicate).accepts(matchValues);
  }

  @Test
  public void should_pass_when_predicate_accepts_all_values() {
    DoublePredicate predicate = val -> val <= 2;

    assertThat(predicate).accepts(1.0, 2.0);
  }

  @Override
  protected DoublePredicateAssert invoke_api_method() {
    return assertions.accepts(1.0, 2.0);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(1.0D, 2.0D), wrapped, PredicateDescription.GIVEN);
  }
}
