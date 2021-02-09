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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.guava.internal;

import static com.google.common.collect.ImmutableRangeSet.builder;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.open;
import static com.google.common.collect.TreeRangeSet.create;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.junit.jupiter.api.BeforeEach;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * Base test for the {@link RangeSets} tests. 
 * 
 * @author Ilya_Koshaleu
 */
public abstract class RangeSetsBaseTest {

  private static final AssertionInfo ASSERTION_INFO = new WritableAssertionInfo();

  protected RangeSet<Integer> actual;
  protected RangeSets rangeSets;

  @BeforeEach
  public void setUp() {
    this.actual = create();
    this.actual.add(closed(1, 10));
    this.actual.add(closed(15, 20));
    this.actual.add(open(30, 35));

    this.rangeSets = new RangeSets();
  }

  protected AssertionInfo someInfo() {
    return ASSERTION_INFO;
  }

  @SafeVarargs
  protected final <T extends Comparable<?>> RangeSet<T> rangeSet(Range<T>... ranges) {
    ImmutableRangeSet.Builder<T> builder = builder();
    for (Range<T> range : ranges) {
      builder.add(range);
    }
    return builder.build();
  }
}
