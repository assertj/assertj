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
package org.assertj.core.api.predicate;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.PredicateAssert;
import org.assertj.core.api.PredicateAssertBaseTest;
import org.assertj.core.util.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldNotMatch.elementsShouldNotMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * @author Filip Hrisafov
 */
public class PredicateAssert_noneMatch_Test extends PredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    thrown.expectAssertionError(actualIsNull());
    LinkedHashSet<String> acceptedValues = Sets.newLinkedHashSet("first", "second");

    assertThat((Predicate<String>) null).noneMatch(acceptedValues);
  }

  @Test
  public void should_fail_when_predicate_matches_some_value() {
    Set<String> acceptedValues = Sets.newLinkedHashSet("first", "second");
    Predicate<String> predicate = acceptedValues::contains;
    Set<String> matchValues = Sets.newHashSet(acceptedValues);
    matchValues.add("third");
    thrown.expectAssertionError(elementsShouldNotMatch(matchValues, "first").create());
    assertThat(predicate).noneMatch(matchValues);
  }

  @Test
  public void should_pass_when_predicate_matches_no_value() {
    Set<String> acceptedValues = Sets.newLinkedHashSet("first", "second");
    Predicate<String> predicate = acceptedValues::contains;
    assertThat(predicate).noneMatch(Sets.newLinkedHashSet("third", "fourth"));
  }

  @Override
  protected PredicateAssert<Boolean> invoke_api_method() {
    return assertions.noneMatch(newArrayList(false, false));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertNoneMatch(getInfo(assertions), newArrayList(false, false), getActual(assertions));
  }
}
