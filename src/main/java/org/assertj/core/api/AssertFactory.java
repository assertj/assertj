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
package org.assertj.core.api;

/**
 * A single method factory interface to create an {@link Assert} class for a given type.
 * This factory method typically wraps a call to <code>assertThat(t)</code> to map to the concrete assert type {@code ASSERT}
 * for the element {@code T}.
 * <p>
 * This interface is typically used by navigation assertions on iterable types like {@link AbstractIterableAssert} when calling
 * {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable&lt;E&gt;, AssertFactory&lt;E, ASSERT&gt;)}
 * <p>
 * @param <T> the type to create assertions for.
 * @param <ASSERT> the assertions class for T.
 * 
 * @since 2.5.0 / 3.5.0
 */
public interface AssertFactory<T, ASSERT> {

  /**
   * Creates the custom Assert object for the given element value.
   *
   * Typically this will just invoke <code>assertThat(t)</code>
   * @param t the type to convert to an Assert object
   * @return returns <code>assertThat(t)</code>
   */
  ASSERT createAssert(T t);
}
