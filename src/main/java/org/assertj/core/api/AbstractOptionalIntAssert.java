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
package org.assertj.core.api;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Integers;
import org.assertj.core.util.VisibleForTesting;

import java.util.OptionalInt;

import static org.assertj.core.error.OptionalIntShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.OptionalIntShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalIntShouldHaveValue.shouldHaveValue;

/**
 * Assertions for {@link java.util.OptionalInt}.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 */
public abstract class AbstractOptionalIntAssert<S extends AbstractOptionalIntAssert<S>> extends
    AbstractAssert<S, OptionalInt> {

    @VisibleForTesting
    Integers integers = Integers.instance();

    protected AbstractOptionalIntAssert(OptionalInt actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that there is a value present in the actual {@link java.util.OptionalInt}.
     * </p>
     * Assertion will pass :
     * <p>
     * <pre><code class='java'>
     * assertThat(OptionalInt.of(10)).isPresent();
     * </code></pre>
     * <p>
     * Assertion will fail :
     * <p>
     * <pre><code class='java'>
     * assertThat(OptionalInt.empty()).isPresent();
     * assertThat((OptionalInt)null).isPresent();
     * </code></pre>
     *
     * @return this assertion object.
     * @throws AssertionError if actual value is empty.
     * @throws AssertionError if actual is null.
     */
    public S isPresent() {
        isNotNull();
        if (!actual.isPresent()) throw failure(shouldBePresent());
        return myself;
    }

    /**
     * Verifies that the actual {@link java.util.OptionalInt} is empty.
     * </p>
     * Assertion will pass :
     * <p>
     * <pre><code class='java'>
     * assertThat(Optional.empty()).isEmpty();
     * </code></pre>
     * <p>
     * Assertion will fail :
     * <p>
     * <pre><code class='java'>
     * assertThat(OptionalInt.of(10)).isEmpty();
     * assertThat((OptionalInt)null).isEmpty();
     * </code></pre>
     *
     * @return this assertion object.
     *
     * @throws AssertionError if actual value is present.
     * @throws AssertionError if actual is null.
     */
    public S isEmpty() {
        isNotNull();
        if (actual.isPresent()) throw failure(shouldBeEmpty(actual));
        return myself;
    }

    /**
     * Verifies that the actual {@link java.util.OptionalInt} has the value in argument.
     * </p>
     * Assertion will pass :
     * <p>
     * <pre><code class='java'>
     * assertThat(OptionalInt.of(8)).hasValue(8);
     * assertThat(OptionalInt.of(8)).hasValue(Integer.valueOf(8));
     * </code></pre>
     * <p>
     * Assertion will fail :
     * <p>
     * <pre><code class='java'>
     * assertThat(OptionalInt.empty()).hasValue(8);
     * assertThat((OptionalInt)null).hasValue(8);
     * assertThat(OptionalInt.of(7)).hasValue(8);
     * </code></pre>
     *
     * @param expectedValue the expected value inside the {@link java.util.OptionalInt}.
     * @return this assertion object.
     *
     * @throws AssertionError if actual value is empty.
     * @throws AssertionError if actual is null.
     * @throws AssertionError if actual has not the value as expected.
     */
    public S hasValue(int expectedValue) {
        isNotNull();
        if (!actual.isPresent()) throw failure(shouldHaveValue(expectedValue));
        //Reuses integers functionality, catches possible assertionerror and throw correct one
        try {
            integers.assertEqual(info, actual.getAsInt(), expectedValue);
        } catch (AssertionError assertionError) {
            throw Failures.instance().failure(info, shouldHaveValue(actual, expectedValue));
        }
        return myself;
    }
}
