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
package org.assertj.core.internal;

import java.util.Objects;

/**
 * Immutable class modeling the actual and expected elements at a given index.
 */
public class IndexedDiff {
  public final Object actual;
  public final Object expected;
  public final int index;

  /**
   * Create a {@link IndexedDiff}.
   * @param actual the actual value of the diff.
   * @param expected the expected value of the diff.
   * @param index the index the diff occurred at.
   */
  public IndexedDiff(Object actual, Object expected, int index) {
    this.actual = actual;
    this.expected = expected;
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IndexedDiff that = (IndexedDiff) o;
    return index == that.index && Objects.equals(actual, that.actual) && Objects.equals(expected, that.expected);
  }

  @Override
  public String toString() {
    return String.format("IndexedDiff(actual=%s, expected=%s, index=%s)", this.actual, this.expected, this.index);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actual, expected, index);
  }

}
