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

import static org.assertj.core.util.Preconditions.checkState;

import java.util.List;

/**
 * Initially copied from https://code.google.com/p/java-diff-utils/.
 * <p>
 * Describes the add-delta between original and revised texts.
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param <T>
 *            The type of the compared elements in the 'lines'.
 */
public class InsertDelta<T> extends Delta<T> {

  /**
   * Creates an insert delta with the two given chunks.
   *
   * @param original
   *            The original chunk. Must not be {@code null}.
   * @param revised
   *            The original chunk. Must not be {@code null}.
   */
  public InsertDelta(Chunk<T> original, Chunk<T> revised) {
    super(original, revised);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applyTo(List<T> target) {
    verify(target);
    int position = this.getOriginal().getPosition();
    List<T> lines = this.getRevised().getLines();
    for (int i = 0; i < lines.size(); i++) {
      target.add(position + i, lines.get(i));
    }
  }

  @Override
  public void verify(List<T> target) throws IllegalStateException {
    checkState(getOriginal().getPosition() <= target.size(),
               "Incorrect patch for delta: delta original position > target size");
  }

  @Override
  public TYPE getType() {
    return Delta.TYPE.INSERT;
  }

}
