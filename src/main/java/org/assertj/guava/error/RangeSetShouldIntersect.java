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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.guava.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import com.google.common.collect.RangeSet;

/**
 * Creates an error message indicating that the given {@link com.google.common.collect.RangeSet} does not intersect
 * neither another one {@link com.google.common.collect.RangeSet} nor some set of
 * {@link com.google.common.collect.Range}.
 *
 * @author Ilya Koshaleu
 */
public class RangeSetShouldIntersect extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldIntersect(RangeSet<?> actual, Object expected, Iterable<?> notIntersected) {
    return new RangeSetShouldIntersect(actual, expected, notIntersected);
  }

  /**
   * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
   *
   * @param actual actual {@link com.google.common.collect.RangeSet}.
   * @param expected expected {@link com.google.common.collect.RangeSet} that have to be intersected.
   * @param notIntersected not intersected ranges.
   */
  private RangeSetShouldIntersect(Object actual, Object expected, Object notIntersected) {
    super("%nExpecting:%n  <%s>%nto intersect%n  <%s>%nbut it does not intersect%n  <%s>%n",
          actual, expected, notIntersected);
  }
}
