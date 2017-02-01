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
package org.assertj.core.util.diff;

import java.util.List;

/**
 * Copy from https://code.google.com/p/java-diff-utils/.
 * <p>
 * The general interface for computing diffs between two lists of elements of type T. 
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param <T> The type of the compared elements in the 'lines'.
 */
public interface DiffAlgorithm<T> {

  /**
   * Computes the difference between the original sequence and the revised
   * sequence and returns it as a {@link Patch} object.
   * 
   * @param original The original sequence. Must not be {@code null}.
   * @param revised The revised sequence. Must not be {@code null}.
   * @return The patch representing the diff of the given sequences. Never {@code null}.
   */
  Patch<T> diff(List<T> original, List<T> revised);
}
