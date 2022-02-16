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
 * @author Ilya Koshaleu
 */
public class RangeSetShouldNotEnclose extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldNotEnclose(Object actual, Object expected, Iterable<?> enclosed) {
    return new RangeSetShouldNotEnclose(actual, expected, enclosed);
  }

  /**
   * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
   * 
   * @param actual actual {@code RangeSet}.
   * @param expected expected value.
   * @param enclosed list of values that haven't to be enclosed, but they have. 
   */
  private RangeSetShouldNotEnclose(Object actual, Object expected, Object enclosed) {
    super("%nExpecting:%n  %s%nnot to enclose%n  %s%nbut it encloses%n  %s%n", actual, expected, enclosed);
  }
}
