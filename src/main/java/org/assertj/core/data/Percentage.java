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
package org.assertj.core.data;

import java.math.BigDecimal;

import static org.assertj.core.util.Objects.*;
import static org.assertj.core.util.Preconditions.checkNotNull;

/**
 * A percentage value between zero and hundred.
 *
 * @param <T> the type of the percentage value.
 * @author Alexander Bishcof
 */
public class Percentage<T extends Number> {
    public final T value;

    /**
     * Creates a new {@link org.assertj.core.data.Percentage}.
     *
     * @param value the value of the percentage.
     * @return the created {@code Percentage}.
     * @throws NullPointerException     if the given value is {@code null}.
     * @throws IllegalArgumentException if the given value is negative or greater hundred.
     */
    public static <T extends Number> Percentage<T> withPercentage(T value) {
        checkNotNull(value);
        checkBoundaries(value.doubleValue());
        return new Percentage<>(value);
    }

    private static void checkBoundaries(double value) {
        if (value < 0 || value > 100)
            throw new IllegalArgumentException(
                String.format("The percentage value <%s> should be between 0 and 100.", value));
    }

    private Percentage(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Percentage<?> other = (Percentage<?>) obj;
        return areEqual(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = HASH_CODE_PRIME * result + hashCodeFor(value);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s[value=%s]", getClass().getSimpleName(), value);
    }

}
