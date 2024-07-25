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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.data.Index;

/**
 * Assertions methods applicable to indexed groups of objects (e.g. arrays or lists.)
 *
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ELEMENT> the type of elements of the "actual" value.
 */
public interface IndexedObjectEnumerableAssert<SELF extends IndexedObjectEnumerableAssert<SELF, ELEMENT>, ELEMENT> extends
    ObjectEnumerableAssert<SELF, ELEMENT> {

  /**
   * Verifies that the actual group contains the given object at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).contains(vilya, atIndex(0));
   * assertThat(elvesRings).contains(nenya, atIndex(1));
   * assertThat(elvesRings).contains(narya, atIndex(2));
   *
   * // assertions will fail
   * assertThat(elvesRings).contains(vilya, atIndex(1));
   * assertThat(elvesRings).contains(nenya, atIndex(2));
   * assertThat(elvesRings).contains(narya, atIndex(0));</code></pre>
   *
   * @param value the object to look for.
   * @param index the index where the object should be stored in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the actual
   *           group.
   * @throws AssertionError if the actual group does not contain the given object at the given index.
   */
  SELF contains(ELEMENT value, Index index);

  /**
   * Verifies that the actual group does not contain the given object at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).contains(vilya, atIndex(1));
   * assertThat(elvesRings).contains(nenya, atIndex(2));
   * assertThat(elvesRings).contains(narya, atIndex(0));
   *
   * // assertions will fail
   * assertThat(elvesRings).contains(vilya, atIndex(0));
   * assertThat(elvesRings).contains(nenya, atIndex(1));
   * assertThat(elvesRings).contains(narya, atIndex(2));</code></pre>
   *
   * @param value the object to look for.
   * @param index the index where the object should not be stored in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual group contains the given object at the given index.
   */
  SELF doesNotContain(ELEMENT value, Index index);
}
