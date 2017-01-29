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
package org.assertj.core.util.diff.myers;

/**
 * Copy from https://code.google.com/p/java-diff-utils/.
 * <p>
 * Specifies when two compared elements in the Myers algorithm are equal.
 * 
 * @param <T> The type of the compared elements in the 'lines'.
 */
public interface Equalizer<T> {

  /**
   * Indicates if two elements are equal according to the diff mechanism.
   * @param original The original element. Must not be {@code null}.
   * @param revised The revised element. Must not be {@code null}.
   * @return Returns true if the elements are equal.
   */
  boolean equals(T original, T revised);
}
