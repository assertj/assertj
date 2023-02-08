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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.guava.api;

import org.junit.jupiter.api.BeforeEach;

import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

public class RangeMapAssertBaseTest {

  protected TreeRangeMap<Integer, String> actual;

  @BeforeEach
  public void setUp() {
    actual = TreeRangeMap.create();
    actual.put(Range.closedOpen(380, 450), "violet");
    actual.put(Range.closedOpen(450, 495), "blue");
    actual.put(Range.closedOpen(495, 570), "green");
    actual.put(Range.closedOpen(570, 590), "yellow");
    actual.put(Range.closedOpen(590, 620), "orange");
    actual.put(Range.closedOpen(620, 750), "red");
  }
}
