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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.RangeSet;

/**
 * Creates an error message indicating that the given {@link com.google.common.collect.RangeSet} does not intersect
 * at lease one element of expected objects.
 *
 * @author Ilya Koshaleu
 */
public class RangeSetShouldIntersectAnyOf extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldIntersectAnyOf(RangeSet<?> actual, Object expected) {
    return new RangeSetShouldIntersectAnyOf(actual, expected);
  }

  /**
   * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
   *
   * @param actual actual {@link com.google.common.collect.RangeSet}.
   * @param expected expected range to intersect.
   */
  private RangeSetShouldIntersectAnyOf(Object actual, Object expected) {
    super("%nExpecting:%n  %s%nto intersect at least one range of the given:%n  %s%n", actual, expected);
  }
}
