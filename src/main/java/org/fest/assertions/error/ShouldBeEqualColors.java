/*
 * Created on Jan 22, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.data.*;

/**
 * Creates an error message indicating that an assertion that verifies that a two colors are equal at a given point fails.
 * 
 * @author Yvonne Wang
 */
public class ShouldBeEqualColors extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEqualColors}</code>.
   * @param expected the expected color.
   * @param actual the actual color.
   * @param point the point where {@code expected} and {@code actual} were compared at.
   * @param offset helps decide if two colors are similar: two colors that are identical to the human eye may still have slightly
   *          different color values. For example, by using an offset of 1 we can indicate that a blue value of 60 is similar to a
   *          blue value of 61.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualColors(RgbColor expected, RgbColor actual, Point point, Offset<?> offset) {
    return new ShouldBeEqualColors(expected, actual, point, offset);
  }

  private ShouldBeEqualColors(RgbColor expected, RgbColor actual, Point point, Offset<?> offset) {
    super("expected:<%s> but was:<%s> at:<%s> within offset:<%s>", expected, actual, point, offset.value);
  }
}
