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
 * A node in a diffpath.
 *
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 *
 * @see DiffNode
 * @see Snake
 */
public abstract class PathNode {

  /** Position in the original sequence. */
  public final int i;
  /** Position in the revised sequence. */
  public final int j;
  /** The previous node in the path. */
  public final PathNode prev;

  /**
   * Concatenates a new path node with an existing diffpath.
   * @param i The position in the original sequence for the new node.
   * @param j The position in the revised sequence for the new node.
   * @param prev The previous node in the path.
   */
  public PathNode(int i, int j, PathNode prev) {
    this.i = i;
    this.j = j;
    this.prev = prev;
  }

  /**
   * Is this node a {@link Snake Snake node}?
   * @return true if this is a {@link Snake Snake node}
   */
  public abstract boolean isSnake();

  /**
   * Is this a bootstrap node?
   * <p>
   * In bootstrap nodes one of the two coordinates is
   * less than zero.
   * @return tru if this is a bootstrap node.
   */
  public boolean isBootstrap() {
    return i < 0 || j < 0;
  }

  /**
   * Skips sequences of {@link DiffNode DiffNodes} until a
   * {@link Snake} or bootstrap node is found, or the end
   * of the path is reached.
   * @return The next first {@link Snake} or bootstrap node in the path, or
   * <code>null</code>
   * if none found.
   */
  public final PathNode previousSnake() {
    if (isBootstrap())
      return null;
    if (!isSnake() && prev != null)
      return prev.previousSnake();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuilder buf = new StringBuilder("[");
    PathNode node = this;
    while (node != null) {
      buf.append("(");
      buf.append(Integer.toString(node.i));
      buf.append(",");
      buf.append(Integer.toString(node.j));
      buf.append(")");
      node = node.prev;
    }
    buf.append("]");
    return buf.toString();
  }
}
