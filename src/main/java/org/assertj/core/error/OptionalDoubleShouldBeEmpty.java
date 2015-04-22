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

import java.util.OptionalDouble;

/**
 * Build error message when an {@link java.util.OptionalDouble} should be empty.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public class OptionalDoubleShouldBeEmpty extends BasicErrorMessageFactory {

    private OptionalDoubleShouldBeEmpty(OptionalDouble optionalValue) {
        super("%nExpecting an empty OptionalDouble but was containing value: <%s>.", optionalValue.getAsDouble());
    }

    /**
     * Indicates that the provided {@link java.util.OptionalDouble} should be empty.
     *
     * @param expectedValue the actual {@link java.util.OptionalDouble} to test.
     * @return a error message factory.
     */
    public static OptionalDoubleShouldBeEmpty shouldBeEmpty(OptionalDouble expectedValue) {
        return new OptionalDoubleShouldBeEmpty(expectedValue);
    }
}
