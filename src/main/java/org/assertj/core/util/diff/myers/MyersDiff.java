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

import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.diff.ChangeDelta;
import org.assertj.core.util.diff.Chunk;
import org.assertj.core.util.diff.DeleteDelta;
import org.assertj.core.util.diff.Delta;
import org.assertj.core.util.diff.DiffAlgorithm;
import org.assertj.core.util.diff.InsertDelta;
import org.assertj.core.util.diff.Patch;

/**
 * Copy from https://code.google.com/p/java-diff-utils/.
 * <p>
 * A clean-room implementation of <a href="http://www.cs.arizona.edu/people/gene/">
 * Eugene Myers</a> differencing algorithm.
 *
 * <p> See the paper at <a href="http://www.cs.arizona.edu/people/gene/PAPERS/diff.ps">
 * http://www.cs.arizona.edu/people/gene/PAPERS/diff.ps</a></p>
 *
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @param <T> The type of the compared elements in the 'lines'.
 */
public class MyersDiff<T> implements DiffAlgorithm<T> {

  /** The equalizer. */
  private final Equalizer<T> equalizer;

  /**
   * Constructs an instance of the Myers differencing algorithm.
   */
  public MyersDiff() {
    /**	Default equalizer. */
    equalizer = new Equalizer<T>() {

      @Override
      public boolean equals(T original, T revised) {
        return original.equals(revised);
      }

    };
  }

  /**
   * {@inheritDoc}
   *
   * Return empty diff if get the error while procession the difference.
   */
  public Patch<T> diff(final List<T> original, final List<T> revised) {
    checkArgument(original != null, "original list must not be null");
    checkArgument(revised != null, "revised list must not be null");
    PathNode path;
    try {
      path = buildPath(original, revised);
      return buildRevision(path, original, revised);
    } catch (IllegalStateException e) {
      e.printStackTrace();
      return new Patch<>();
    }
  }

  /**
   * Computes the minimum diffpath that expresses de differences
   * between the original and revised sequences, according
   * to Gene Myers differencing algorithm.
   *
   * @param orig The original sequence.
   * @param rev The revised sequence.
   * @return A minimum {@link PathNode Path} across the differences graph.
   * @throws IllegalStateException if a diff path could not be found.
   */
  public PathNode buildPath(final List<T> orig, final List<T> rev) {
    checkArgument(orig != null, "original sequence is null");
    checkArgument(rev != null, "revised sequence is null");

    // these are local constants
    final int N = orig.size();
    final int M = rev.size();

    final int MAX = N + M + 1;
    final int size = 1 + 2 * MAX;
    final int middle = size / 2;
    final PathNode diagonal[] = new PathNode[size];

    diagonal[middle + 1] = new Snake(0, -1, null);
    for (int d = 0; d < MAX; d++) {
      for (int k = -d; k <= d; k += 2) {
        final int kmiddle = middle + k;
        final int kplus = kmiddle + 1;
        final int kminus = kmiddle - 1;
        PathNode prev;

        int i;
        if ((k == -d) || (k != d && diagonal[kminus].i < diagonal[kplus].i)) {
          i = diagonal[kplus].i;
          prev = diagonal[kplus];
        } else {
          i = diagonal[kminus].i + 1;
          prev = diagonal[kminus];
        }

        diagonal[kminus] = null; // no longer used

        int j = i - k;

        PathNode node = new DiffNode(i, j, prev);

        // orig and rev are zero-based
        // but the algorithm is one-based
        // that's why there's no +1 when indexing the sequences
        while (i < N && j < M && equals(orig.get(i), rev.get(j))) {
          i++;
          j++;
        }
        if (i > node.i) node = new Snake(i, j, node);

        diagonal[kmiddle] = node;

        if (i >= N && j >= M) return diagonal[kmiddle];
      }
      diagonal[middle + d - 1] = null;
    }

    // According to Myers, this cannot happen
    throw new IllegalStateException("could not find a diff path");
  }

  private boolean equals(T orig, T rev) {
    return equalizer.equals(orig, rev);
  }

  /**
   * Constructs a {@link Patch} from a difference path.
   *
   * @param path The path.
   * @param orig The original sequence.
   * @param rev The revised sequence.
   * @return A {@link Patch} script corresponding to the path.
   */
  public Patch<T> buildRevision(PathNode path, List<T> orig, List<T> rev) {
    checkArgument(path != null, "path is null");
    checkArgument(orig != null, "original sequence is null");
    checkArgument(rev != null, "revised sequence is null");

    Patch<T> patch = new Patch<>();
    if (path.isSnake())
      path = path.prev;
    while (path != null && path.prev != null && path.prev.j >= 0) {
      checkState(!path.isSnake(), "bad diffpath: found snake when looking for diff");
      int i = path.i;
      int j = path.j;

      path = path.prev;
      int ianchor = path.i;
      int janchor = path.j;

      Chunk<T> original = new Chunk<>(ianchor, copyOfRange(orig, ianchor, i));
      Chunk<T> revised = new Chunk<>(janchor, copyOfRange(rev, janchor, j));
      Delta<T> delta;
      if (original.size() == 0 && revised.size() != 0) {
        delta = new InsertDelta<>(original, revised);
      } else if (original.size() > 0 && revised.size() == 0) {
        delta = new DeleteDelta<>(original, revised);
      } else {
        delta = new ChangeDelta<>(original, revised);
      }

      patch.addDelta(delta);
      if (path.isSnake()) path = path.prev;
    }
    return patch;
  }

  /**
   * Creates a new list containing the elements returned by {@link List#subList(int, int)}.
   * @param original The original sequence. Must not be {@code null}.
   * @param fromIndex low endpoint (inclusive) of the subList.
   * @param to high endpoint (exclusive) of the subList.
   * @return A new list of the specified range within the original list.
  
   */
  private List<T> copyOfRange(final List<T> original, final int fromIndex, final int to) {
    return new ArrayList<>(original.subList(fromIndex, to));
  }
}
