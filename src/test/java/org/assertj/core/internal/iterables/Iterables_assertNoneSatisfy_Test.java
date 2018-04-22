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


import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Lists.newArrayList;

public class Iterables_assertNoneSatisfy_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

  @Test
  public void should_satisfy_single_requirement() {
    iterables.assertNoneSatisfy(someInfo(), actual, s -> assertThat(s.length()).isEqualTo(5));
  }

  @Test
  public void should_satisfy_multiple_requirements() {
    iterables.assertNoneSatisfy(someInfo(), actual, s -> {
      assertThat(s.length()).isEqualTo(5);
      assertThat(s).contains("V");
    });
  }

  @Test
  public void should_fail_according_to_requirements() {
    Consumer<String> restrictions = s -> {
      assertThat(s).isNotBlank();
    };
    try {
      iterables.assertNoneSatisfy(someInfo(), actual, restrictions);
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("Expecting no elements of:");
      assertThat(e.getMessage()).contains("<[\"Luke\", \"Leia\", \"Yoda\"]>");
      assertThat(e.getMessage()).contains("to satisfy the given assertions requirements but one did.");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }



}
