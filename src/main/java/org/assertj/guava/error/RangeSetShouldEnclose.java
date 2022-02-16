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

/**
 * Creates an error message indicating that the given {@link com.google.common.collect.RangeSet} does not enclose 
 * neither another one {@link com.google.common.collect.RangeSet} nor some set of 
 * {@link com.google.common.collect.Range}.
 *
 * @author Ilya Koshaleu
 */
public class RangeSetShouldEnclose extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldEnclose(Object actual, Object expected, Iterable<?> notEnclosed) {
    return new RangeSetShouldEnclose(actual, expected, notEnclosed);
  }

  /**
   * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
   *
   * @param actual actual {@code RangeSet}.
   * @param expected expected range to check for enclosing.
   * @param notEnclosed list of objects that have to be enclosed, but they haven't.
   */
  private RangeSetShouldEnclose(Object actual, Object expected, Object notEnclosed) {
    super("%nExpecting:%n  %s%nto enclose%n  %s%nbut it does not enclose%n  %s%n", actual, expected, notEnclosed);
  }
}
