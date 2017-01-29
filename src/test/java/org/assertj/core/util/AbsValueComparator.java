/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static java.lang.Math.abs;

import java.util.Comparator;

public class AbsValueComparator<NUMBER extends Number> implements Comparator<NUMBER> {

  @Override
  public int compare(NUMBER i1, NUMBER i2) {
    double diff = abs(i1.doubleValue()) - abs(i2.doubleValue());
    if (diff == 0.0) return 0;
    return diff < 0.0 ? -1 : 1;
  }
}
