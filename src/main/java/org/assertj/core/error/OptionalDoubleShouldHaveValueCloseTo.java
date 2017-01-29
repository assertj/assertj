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

import org.assertj.core.data.Offset;

import java.util.OptionalDouble;

/**
 * Build error message when an {@link java.util.OptionalDouble} should have a specific value close to an offset.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public class OptionalDoubleShouldHaveValueCloseTo extends BasicErrorMessageFactory {

    private OptionalDoubleShouldHaveValueCloseTo(OptionalDouble actual, double expected, Offset<Double> offset,
                                                 double difference) {
        super("%nExpecting:%n  <%s>%nto be close to:%n  <%s>%n" +
              "by less than <%s> but difference was <%s>.%n" +
              "(a difference of exactly <%s> being considered valid)",
              actual, expected, offset.value, difference, offset.value);
    }

    private OptionalDoubleShouldHaveValueCloseTo(double expected) {
        super("%nExpecting an OptionalDouble with value:%n  <%s>%nbut was empty.", expected);
    }

    /**
     * Indicates that the provided {@link java.util.OptionalDouble} does not have the provided argument.
     *
     * @param optional      the {@link java.util.OptionalDouble} which has a value.
     * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalDouble}.
     * @param offset        the given positive offset.
     * @param difference    the effective difference between actual and expected.
     * @return a error message factory
     */
    public static OptionalDoubleShouldHaveValueCloseTo shouldHaveValueCloseTo(OptionalDouble optional,
                                                                              double expectedValue,
                                                                              Offset<Double> offset,
                                                                              double difference) {
        return new OptionalDoubleShouldHaveValueCloseTo(optional, expectedValue, offset, difference);
    }

    /**
     * Indicates that an {@link java.util.OptionalDouble} is empty so it doesn't have the expected value.
     *
     * @param expectedValue the value we expect to be in an {@link java.util.OptionalDouble}.
     * @return a error message factory.
     */
    public static OptionalDoubleShouldHaveValueCloseTo shouldHaveValueCloseTo(double expectedValue) {
        return new OptionalDoubleShouldHaveValueCloseTo(expectedValue);
    }
}
