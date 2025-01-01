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
package org.assertj.core.data;

import static java.lang.String.format;
import static org.assertj.core.util.Preconditions.checkArgument;

/**
 * A positive percentage value.
 *
 * @author Alexander Bishcof
 */
public final class Percentage {

  public final double value;

  /**
   * Creates a new {@link org.assertj.core.data.Percentage}.
   *
   * @param value the value of the percentage.
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Percentage withPercentage(double value) {
    checkArgument(value >= 0, "The percentage value <%s> should be greater than or equal to zero", value);
    return new Percentage(value);
  }

  private Percentage(double value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Percentage)) return false;
    Percentage other = (Percentage) obj;
    return Double.compare(value, other.value) == 0;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(value);
  }

  @Override
  public String toString() {
    return noFractionalPart() ? format("%s%%", (int) value) : format("%s%%", value);
  }

  private boolean noFractionalPart() {
    return (value % 1) == 0;
  }

}
