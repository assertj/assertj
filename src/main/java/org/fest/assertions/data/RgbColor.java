/*
 * Created on Oct 24, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.data;

/**
 * A color.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class RgbColor {

  /** The red component of this color. */
  public final int r;

  /** The green component of this color. */
  public final int g;

  /** The blue component of this color. */
  public final int b;

  /**
   * Creates a new </code>{@link RgbColor}</code>.
   * @param rgb a value representing a RGB combination.
   */
  public RgbColor(int rgb) {
    this(r(rgb), g(rgb), b(rgb));
  }

  private static int r(int rgb) {
    return color(rgb, 16);
  }

  private static int g(int rgb) {
    return color(rgb, 8);
  }

  private static int b(int rgb) {
    return color(rgb, 0);
  }

  private static int color(int rgb, int c) {
    return (rgb >> c) & 0xFF;
  }

  /**
   * Creates a new </code>{@link RgbColor}</code> with the specified red, green, and blue values in the range (0 - 255.)
   * @param r the red component.
   * @param g the green component.
   * @param b the blue component.
   * @throws IllegalArgumentException if {@code r}, {@code g} or {@code b} are outside of the range 0 to 255, inclusive.
   */
  public RgbColor(int r, int g, int b) {
    validateIsInRange("R", r);
    validateIsInRange("G", g);
    validateIsInRange("B", b);
    this.r = r;
    this.g = g;
    this.b = b;
  }

  private void validateIsInRange(String name, int value) {
    if (value >= 0 && value <= 255) return;
    throw new IllegalArgumentException(String.format("%s should be between 0 and 255 but was %d", name, value));
  }
}
