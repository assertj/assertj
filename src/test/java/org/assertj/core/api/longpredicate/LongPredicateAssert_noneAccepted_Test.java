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
package org.assertj.core.api.longpredicate;

import java.util.List;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.api.LongPredicateAssert;
import org.assertj.core.api.LongPredicateAssertBaseTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * @author Filip Hrisafov
 */
public class LongPredicateAssert_noneAccepted_Test extends LongPredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());
    long[] acceptedValues = new long[] { 1, 2, 3 };

    assertThat((LongPredicate) null).noneAccepted(acceptedValues);
  }

  @Test
  public void should_fail_when_predicate_accepts_some_value() {
    LongPredicate predicate = num -> num <= 2;
    long[] matchValues = new long[] { 1, 2, 3 };
    List<Long> matchValuesList = LongStream.of(matchValues).boxed().collect(Collectors.toList());
    thrown.expectAssertionError(noElementsShouldMatch(matchValuesList, 1L).create());
    assertThat(predicate).noneAccepted(matchValues);
  }

  @Test
  public void should_pass_when_predicate_accepts_no_value() {
    LongPredicate predicate = num -> num <= 2;
    long[] acceptedValues = new long[] { 3, 4, 5 };
    assertThat(predicate).noneAccepted(acceptedValues);
  }

  @Override
  protected LongPredicateAssert invoke_api_method() {
    return assertions.noneAccepted(3, 4);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertNoneMatch(getInfo(assertions), newArrayList(3L, 4L), wrapped);
  }
}
