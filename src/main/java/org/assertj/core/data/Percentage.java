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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.data;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Preconditions.checkArgument;

/**
 * A positive percentage value.
 *
 * @author Alexander Bishcof
 */
public class Percentage {

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
    requireNonNull(value);
    checkArgument(value >= 0, "The percentage value <%s> should be greater than or equal to zero", value);
    return new Percentage(value);
  }

  private Percentage(double value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    Percentage that = (Percentage) o;

    return Double.compare(that.value, value) == 0;
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
