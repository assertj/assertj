package org.assertj.core.util.diff.myers;

/**
 * A diffnode in a diffpath.
 * <p>
 * A DiffNode and its previous node mark a delta between
 * two input sequences, that is, two differing subsequences
 * between (possibly zero length) matching sequences.
 *
 * {@link difflib.myers.DiffNode DiffNodes} and {@link difflib.myers.Snake Snakes} allow for compression
 * of diffpaths, as each snake is represented by a single {@link Snake Snake}
 * node and each contiguous series of insertions and deletions is represented
 * by a single {@link difflib.myers.DiffNode DiffNodes}.
 *
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 *
 */
public final class DiffNode extends PathNode {
    /**
     * Constructs a DiffNode.
     * <p>
     * DiffNodes are compressed. That means that
     * the path pointed to by the <code>prev</code> parameter
     * will be followed using {@link PathNode#previousSnake}
     * until a non-diff node is found.
     *
     * @param i the position in the original sequence
     * @param j the position in the revised sequence
     * @param prev the previous node in the path.
     */
    public DiffNode(int i, int j, PathNode prev) {
        super(i, j, (prev == null ? null : prev.previousSnake()));
    }
    
    /**
     * {@inheritDoc}
     * @return false, always
     */
    public boolean isSnake() {
        return false;
    }
    
}