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
 *  Represents a snake in a diffpath.
 * <p>
 *
 * {@link DiffNode DiffNodes} and {@link Snake Snakes} allow for compression
 * of diffpaths, as each snake is represented by a single {@link Snake Snake}
 * node and each contiguous series of insertions and deletions is represented
 * by a single {@link DiffNode DiffNodes}.
 *
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 */
public final class Snake extends PathNode {

  /**
   * Constructs a snake node.
   *
   * @param i the position in the original sequence
   * @param j the position in the revised sequence
   * @param prev the previous node in the path.
   */
  public Snake(int i, int j, PathNode prev) {
    super(i, j, prev);
  }

  /**
   * {@inheritDoc}
   * @return true always
   */
  public boolean isSnake() {
    return true;
  }

}