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
package org.assertj.core.error;

import org.junit.Test;

import java.util.OptionalDouble;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.OptionalDoubleShouldHaveValueCloseTo.shouldHaveValueCloseTo;

public class OptionalDoubleShouldHaveValueCloseTo_create_Test {

    @Test
    public void should_create_error_message_when_optionaldouble_is_empty() throws Exception {
        String errorMessage = shouldHaveValueCloseTo(10.0).create();
        assertThat(errorMessage).isEqualTo(format("%nExpecting an OptionalDouble with value:%n" +
                                                  "  <10.0>%n" +
                                                  "but was empty."));
    }

    @Test
    public void should_create_error_message() throws Exception {
        String errorMessage = shouldHaveValueCloseTo(OptionalDouble.of(20.0), 10.0, within(2.0), 3).create();
        assertThat(errorMessage).isEqualTo(format("%nExpecting:%n  <OptionalDouble[20.0]>%nto be close to:%n  <10.0>%n" +
                                                  "by less than <2.0> but difference was <3.0>.%n" +
                                                  "(a difference of exactly <2.0> being considered valid)"));
    }
}
