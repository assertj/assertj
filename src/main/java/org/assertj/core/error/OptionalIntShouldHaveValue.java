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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;

import java.util.OptionalInt;

/**
 * Build error message when an {@link java.util.OptionalInt} should have a specific value.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public class OptionalIntShouldHaveValue extends BasicErrorMessageFactory {

    private OptionalIntShouldHaveValue(OptionalInt actual, int expected) {
        super("%nExpecting:%n  <%s>%nto have:%n  <%s>%nbut was not.", actual, expected);
    }

    private OptionalIntShouldHaveValue(int expected) {
        super("%nExpecting an OptionalInt with value:%n  <%s>%nbut was empty.", expected);
    }

    /**
     * Indicates that the provided {@link java.util.OptionalInt} does not have the provided argument.
     *
     * @param optional      the {@link java.util.OptionalInt} which has a value.
     * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalInt}.
     * @return a error message factory
     */
    public static OptionalIntShouldHaveValue shouldHaveValue(OptionalInt optional, int expectedValue) {
        return new OptionalIntShouldHaveValue(optional, expectedValue);
    }

    /**
     * Indicates that an {@link java.util.OptionalInt} is empty so it doesn't have the expected value.
     *
     * @param expectedValue the value we expect to be in an {@link java.util.OptionalInt}.
     * @return a error message factory.
     */
    public static OptionalIntShouldHaveValue shouldHaveValue(int expectedValue) {
        return new OptionalIntShouldHaveValue(expectedValue);
    }
}
