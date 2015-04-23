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

import java.util.OptionalLong;

/**
 * Build error message when an {@link java.util.OptionalLong} should have a specific value.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public class OptionalLongShouldHaveValue extends BasicErrorMessageFactory {

    private OptionalLongShouldHaveValue(OptionalLong actual, long expected) {
        super("%nExpecting:%n  <%s>%nto have:%n  <%s>%nbut was not.", actual, expected);
    }

    private OptionalLongShouldHaveValue(long expected) {
        super("%nExpecting an OptionalLong with value:%n  <%s>%nbut was empty.", expected);
    }

    /**
     * Indicates that the provided {@link java.util.OptionalLong} does not have the provided argument.
     *
     * @param optional      the {@link java.util.OptionalLong} which has a value.
     * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalLong}.
     * @return a error message factory
     */
    public static OptionalLongShouldHaveValue shouldHaveValue(OptionalLong optional, long expectedValue) {
        return new OptionalLongShouldHaveValue(optional, expectedValue);
    }

    /**
     * Indicates that an {@link java.util.OptionalLong} is empty so it doesn't have the expected value.
     *
     * @param expectedValue the value we expect to be in an {@link java.util.OptionalLong}.
     * @return a error message factory.
     */
    public static OptionalLongShouldHaveValue shouldHaveValue(long expectedValue) {
        return new OptionalLongShouldHaveValue(expectedValue);
    }
}
