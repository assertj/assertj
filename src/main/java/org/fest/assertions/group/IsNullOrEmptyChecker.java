/*
 * Created on Oct 7, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.group;

import static org.fest.util.Collections.list;

import java.util.List;

import org.fest.util.VisibleForTesting;

/**
 * Indicates whether an object is {@code null} or an empty group of values.
 *
 * @author Alex Ruiz
 */
public class IsNullOrEmptyChecker {

  @VisibleForTesting final List<IsEmptyChecker> checkers =
    list(ArrayIsEmptyChecker.instance(), CollectionIsEmptyChecker.instance());

  @VisibleForTesting static final IsNullOrEmptyChecker INSTANCE = new IsNullOrEmptyChecker();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static IsNullOrEmptyChecker instance() {
    return INSTANCE;
  }

  /**
   * Indicates whether the given object is {@code null} or an empty group of values.
   * @param o the object to verify.
   * @return {@code true} if the given object is {@code null} or if it is an empty group of values; {@code false}
   * otherwise.
   */
  public boolean isNullOrEmpty(Object o) {
    if (o == null) return true;
    for (IsEmptyChecker checker : checkers) {
      if (!checker.canHandle(o)) continue;
      return checker.isEmpty(o);
    }
    return false;
  }

  @VisibleForTesting IsNullOrEmptyChecker() {}
}
