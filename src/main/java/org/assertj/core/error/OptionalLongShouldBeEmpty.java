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
 * Build error message when an {@link java.util.OptionalLong} should be empty.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public class OptionalLongShouldBeEmpty extends BasicErrorMessageFactory {

    private OptionalLongShouldBeEmpty(OptionalLong optionalValue) {
        super("%nExpecting an empty OptionalLong but was containing value: <%s>.", optionalValue.getAsLong());
    }

    /**
     * Indicates that the provided {@link java.util.OptionalLong} should be empty.
     *
     * @param expectedValue the actual {@link java.util.OptionalLong} to test.
     * @return a error message factory.
     */
    public static OptionalLongShouldBeEmpty shouldBeEmpty(OptionalLong expectedValue) {
        return new OptionalLongShouldBeEmpty(expectedValue);
    }
}
