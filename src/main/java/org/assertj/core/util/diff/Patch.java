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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Copy from https://code.google.com/p/java-diff-utils/.
 * <p>
 * Describes the patch holding all deltas between the original and revised texts.
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param <T> The type of the compared elements in the 'lines'.
 */
public class Patch<T> {
  private List<Delta<T>> deltas = new LinkedList<>();

  /**
   * Apply this patch to the given target
   * @param target the list to patch
   * @return the patched text
   * @throws IllegalStateException if can't apply patch
   */
  public List<T> applyTo(List<T> target) throws IllegalStateException {
    List<T> result = new LinkedList<>(target);
    ListIterator<Delta<T>> it = getDeltas().listIterator(deltas.size());
    while (it.hasPrevious()) {
      Delta<T> delta = it.previous();
      delta.applyTo(result);
    }
    return result;
  }

  /**
   * Add the given delta to this patch
   * @param delta the given delta
   */
  public void addDelta(Delta<T> delta) {
    deltas.add(delta);
  }

  /**
   * Get the list of computed deltas
   * @return the deltas
   */
  public List<Delta<T>> getDeltas() {
    Collections.sort(deltas, DeltaComparator.INSTANCE);
    return deltas;
  }
}
