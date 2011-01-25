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

import static org.fest.assertions.data.Point.atPoint;

import org.fest.assertions.data.*;

/**
 * @author Yvonne Wang
 */
class ColorComparisonResult {

  static final ColorComparisonResult ARE_EQUAL = new ColorComparisonResult();

  static ColorComparisonResult notEqual(RgbColor c1, RgbColor c2, int x, int y) {
    return new ColorComparisonResult(c1, c2, atPoint(x, y));
  }

  final RgbColor color1;
  final RgbColor color2;
  final Point point;

  private ColorComparisonResult() {
    this(null, null, null);
  }

  private ColorComparisonResult(RgbColor c1, RgbColor c2, Point p) {
    color1 = c1;
    color2 = c2;
    point = p;
  }
}
