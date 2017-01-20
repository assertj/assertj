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
package org.assertj.core.internal.iterables;

import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;


public class Iterables_assertAnySatisfy_Test extends IterablesBaseTest {

    private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

    @Test
    public void should_satisfy_single_requirement() {
        iterables.assertAnySatisfy(someInfo(), actual, s -> assertThat(s.length()).isEqualTo(4));
    }

    @Test
    public void should_satisfy_multiple_requirements_one_satisfied() {
        iterables.assertAnySatisfy(someInfo(), actual, s -> {
            assertThat(s.length()).isEqualTo(4);
            assertThat(s).doesNotContain("L");
        });
    }

    @Test
    public void should_satisfy_multiple_requirements_two_satisfied() {
        iterables.assertAnySatisfy(someInfo(), actual, s -> {
            assertThat(s.length()).isEqualTo(4);
            assertThat(s).contains("L");
        });
    }

    @Test
    public void should_fail_according_to_requirements() {
        try {
            iterables.assertAnySatisfy(someInfo(), actual, s -> {
                assertThat(s.length()).isEqualTo(4);
                assertThat(s).contains("W");
            });
        } catch (AssertionError e) {
            verify(failures).failure(info, elementsShouldSatisfyAny(actual));
            return;
        }
        failBecauseExpectedAssertionErrorWasNotThrown();
    }

    @Test
    public void should_fail_if_consumer_is_null() {
        thrown.expectNullPointerException("The Consumer<T> expressing the assertions requirements must not be null");
        assertThat(actual).anySatisfy(null);
    }

    @Test
    public void should_fail_if_actual_is_null() {
        thrown.expectAssertionError(actualIsNull());
        actual = null;
        assertThat(actual).anySatisfy(null);
    }
}