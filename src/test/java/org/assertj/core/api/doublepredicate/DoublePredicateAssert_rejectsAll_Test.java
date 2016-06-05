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

import java.util.List;
import java.util.function.DoublePredicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.assertj.core.api.DoublePredicateAssert;
import org.assertj.core.api.DoublePredicateAssertBaseTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * @author Filip Hrisafov
 */
public class DoublePredicateAssert_rejectsAll_Test extends DoublePredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());
    double[] acceptedValues = new double[] { 1, 2, 3 };

    assertThat((DoublePredicate) null).rejectsAll(acceptedValues);
  }

  @Test
  public void should_fail_when_predicate_accepts_some_value() {
    DoublePredicate predicate = num -> num <= 2;
    double[] matchValues = new double[] { 1, 2, 3 };
    List<Double> matchValuesList = DoubleStream.of(matchValues).boxed().collect(Collectors.toList());
    thrown.expectAssertionError(noElementsShouldMatch(matchValuesList, 1D).create());
    assertThat(predicate).rejectsAll(matchValues);
  }

  @Test
  public void should_pass_when_predicate_accepts_no_value() {
    DoublePredicate predicate = num -> num <= 2;
    double[] acceptedValues = new double[] { 3, 4, 5 };
    assertThat(predicate).rejectsAll(acceptedValues);
  }

  @Override
  protected DoublePredicateAssert invoke_api_method() {
    return assertions.rejectsAll(3, 4);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertNoneMatch(getInfo(assertions), newArrayList(3D, 4D), wrapped);
  }
}
