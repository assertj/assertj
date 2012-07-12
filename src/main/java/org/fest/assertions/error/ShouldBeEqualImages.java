/*
 * Created on Jan 24, 2011
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

import org.fest.assertions.data.Offset;

/**
 * Creates an error message that indicates an assertion that verifies that two images are equal failed.
 * 
 * @author Yvonne Wang
 */
public class ShouldBeEqualImages extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEqualImages}</code>.
   * @param offset helps decide if the color of two pixels are similar: two pixels that are identical to the human eye may still
   *          have slightly different color values. For example, by using an offset of 1 we can indicate that a blue value of 60
   *          is similar to a blue value of 61.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualImages(Offset<Integer> offset) {
    return new ShouldBeEqualImages(offset);
  }

  private ShouldBeEqualImages(Offset<Integer> offset) {
    super("expecting images to be equal within offset:<%s>", offset.value);
  }
}
