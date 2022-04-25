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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Concrete raw comparable assertions to be used through {@link Assertions#assertThatComparable(Comparable)}.
 */
public class RawComparableAssert<ACTUAL> extends AbstractRawComparableAssert<RawComparableAssert<ACTUAL>, ACTUAL> {

  protected RawComparableAssert(Comparable<ACTUAL> actual) {
    super(actual, RawComparableAssert.class);
  }

}
