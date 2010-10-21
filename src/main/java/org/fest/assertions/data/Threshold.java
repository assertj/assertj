/*
 * Created on Mar 25, 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2009-2010 the original author or authors.
 */
package org.fest.assertions.data;

/**
 * A threshold.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public final class Threshold {

  /**
   * Creates a new <code>{@link Threshold}</code>.
   * @param value the value of the threshold.
   * @return the created instance.
   * @throws IllegalArgumentException if the given value is less than zero.
   */
  public static Threshold threshold(int value) {
    if (value < 0) throw new IllegalArgumentException("The value of the threshold should not be negative");
    return new Threshold(value);
  }

  private final int value;

  private Threshold(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
