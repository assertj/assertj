/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.groups;

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.util.Lists.list;

import java.util.Arrays;
import java.util.List;

/** An ordered group of values used by extracting assertions. */
public final class Tuple {

  private final List<Object> values;

  /**
   * Creates a tuple containing the given values.
   *
   * @param values the tuple values
   */
  public Tuple(Object... values) {
    this.values = list(values);
  }

  /**
   * Returns the tuple values as an array.
   *
   * @return the tuple values
   */
  public Object[] toArray() {
    return values.toArray();
  }

  /**
   * Returns the tuple values as a list.
   *
   * @return the tuple values
   */
  public List<Object> toList() {
    return values;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Tuple)) return false;
    Tuple other = (Tuple) obj;
    return Arrays.deepEquals(values.toArray(), other.values.toArray());
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(values.toArray());
  }

  @Override
  public String toString() {
    return CONFIGURATION_PROVIDER.representation().toStringOf(this);
  }

  /**
   * Creates a tuple containing the given values.
   *
   * @param values the tuple values
   * @return the created tuple
   */
  public static Tuple tuple(Object... values) {
    return new Tuple(values);
  }

}
