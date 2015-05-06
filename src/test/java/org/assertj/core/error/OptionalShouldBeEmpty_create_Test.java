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
package org.assertj.core.error;

import org.junit.Test;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;

public class OptionalShouldBeEmpty_create_Test {

    @Test
    public void should_create_error_message_for_optional() throws Exception {
        String errorMessage = shouldBeEmpty(Optional.of("not-empty")).create();
        assertThat(errorMessage).isEqualTo("\nExpecting an empty Optional but was containing value: <\"not-empty\">.");
    }

    @Test
    public void should_create_error_message_for_optionaldouble() throws Exception {
        String errorMessage = shouldBeEmpty(OptionalDouble.of(1)).create();
        assertThat(errorMessage).isEqualTo("\nExpecting an empty Optional but was containing value: <1.0>.");
    }

    @Test
    public void should_create_error_message_for_optionalint() throws Exception {
        String errorMessage = shouldBeEmpty(OptionalInt.of(1)).create();
        assertThat(errorMessage).isEqualTo("\nExpecting an empty Optional but was containing value: <1>.");
    }

    @Test
    public void should_create_error_message_for_optionallong() throws Exception {
        String errorMessage = shouldBeEmpty(Optional.of(1L)).create();
        assertThat(errorMessage).isEqualTo("\nExpecting an empty Optional but was containing value: <1L>.");
    }
}
