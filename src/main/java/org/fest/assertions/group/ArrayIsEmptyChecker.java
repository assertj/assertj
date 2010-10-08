/*
 * Created on Oct 7, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.group;

import static java.lang.reflect.Array.getLength;

/**
 * Indicates whether an array (of {@code Object}s or primitives) is empty or not.
 *
 * @author Alex Ruiz
 */
class ArrayIsEmptyChecker implements IsEmptyChecker {

  private static final ArrayIsEmptyChecker INSTANCE = new ArrayIsEmptyChecker();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static ArrayIsEmptyChecker instance() {
    return INSTANCE;
  }

  private ArrayIsEmptyChecker() {}

  public boolean canHandle(Object o) {
    if (o == null) return false;
    return o.getClass().isArray();
  }

  public boolean isEmpty(Object o) {
    return getLength(o) == 0;
  }
}
