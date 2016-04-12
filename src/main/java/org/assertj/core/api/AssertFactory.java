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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

/**
 * A factory method to create an Assert class for a given type.
 * This factory method typically wraps a call to <code>assertThat(t)</code> to map to the concrete assert type A
 * for the element T.
 * <br>
 * This interface is typically used by navigation assertions on iterable types like {@link NavigationListAssert}
 */
public interface AssertFactory<T, A> {

  /**
   * Creates the custom Assert object for the given element value.
   *
   * Typically this will just invoke <code>assertThat(t)</code>
   * @param t the type to convert to an Assert object
   * @return returns <code>assertThat(t)</code>
   */
  A createAssert(T t);
}
