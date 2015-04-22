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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.optionaldouble;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

import java.util.OptionalDouble;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalDoubleShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class OptionalDoubleAssert_isEmpty_Test extends BaseTest {

    @Test
    public void should_pass_if_optionaldouble_is_empty() throws Exception {
        assertThat(OptionalDouble.empty()).isEmpty();
    }

    @Test
    public void should_fail_when_optionaldouble_is_null() throws Exception {
        thrown.expectAssertionError(actualIsNull());

        assertThat((OptionalDouble) null).isEmpty();
    }

    @Test
    public void should_fail_if_optionaldouble_is_present() throws Exception {
        OptionalDouble actual = OptionalDouble.of(10.0);

        thrown.expectAssertionError(shouldBeEmpty(actual).create());

        assertThat(actual).isEmpty();
    }
}
