/*
 * Created on Jan 21, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal;

import org.fest.assertions.data.RgbColor;

/**
 * @author Yvonne Wang
 */
class ColorComparisonResult {

  static final ColorComparisonResult ARE_EQUAL = new ColorComparisonResult();

  static ColorComparisonResult notEqual(RgbColor c1, RgbColor c2, int x, int y) {
    return new ColorComparisonResult(c1, c2, x, y);
  }

  final RgbColor c1;
  final RgbColor c2;
  final int x;
  final int y;

  private ColorComparisonResult() {
    this(null, null, 0, 0);
  }

  private ColorComparisonResult(RgbColor c1, RgbColor c2, int x, int y) {
    this.c1 = c1;
    this.c2 = c2;
    this.x = x;
    this.y = y;
  }
}
