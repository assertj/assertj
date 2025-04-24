/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

/**
 * {@link Comparable} assertions.
 * <p>
 * This class offers better compatibility than {@link ComparableAssert} and related implementations, currently limited
 * due to the upper bound of {@link ComparableAssert}'s type parameters.
 *
 * @see Assertions#assertThatComparable(Comparable)
 * @since 3.23.0
 */
public class UniversalComparableAssert<T> extends AbstractUniversalComparableAssert<UniversalComparableAssert<T>, T> {

  protected UniversalComparableAssert(Comparable<T> actual) {
    super(actual, UniversalComparableAssert.class);
  }

}
