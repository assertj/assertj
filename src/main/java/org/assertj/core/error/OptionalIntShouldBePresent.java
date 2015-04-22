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

/**
 * Build error message when a value should be present in an {@link java.util.OptionalInt}.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public class OptionalIntShouldBePresent extends BasicErrorMessageFactory {

    private OptionalIntShouldBePresent() {
        super("Expecting OptionalInt to contain a value but was empty.");
    }

    /**
     * Indicates that a value should be present in an empty {@link java.util.OptionalInt}.
     *
     * @return a error message factory.
     */
    public static OptionalIntShouldBePresent shouldBePresent() {
        return new OptionalIntShouldBePresent();
    }
}
