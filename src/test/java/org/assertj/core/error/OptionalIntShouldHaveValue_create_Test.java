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

import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalIntShouldHaveValue.shouldHaveValue;

public class OptionalIntShouldHaveValue_create_Test {

    @Test
    public void should_create_error_message_when_OptionalInt_is_empty() throws Exception {
        String errorMessage = shouldHaveValue(10).create();
        assertThat(errorMessage).isEqualTo("\nExpecting an OptionalInt with value:\n" +
                                           "  <10>\n" +
                                           "but was empty.");
    }

    @Test
    public void should_create_error_message() throws Exception {
        String errorMessage = shouldHaveValue(OptionalInt.of(20), 10).create();
        assertThat(errorMessage).isEqualTo("\nExpecting:\n" +
                                           "  <OptionalInt[20]>\n" +
                                           "to have:\n" +
                                           "  <10>\n" +
                                           "but was not.");
    }
}
