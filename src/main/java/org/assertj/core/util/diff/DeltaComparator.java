package org.assertj.core.util.diff;

import org.assertj.core.util.diff.Delta;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author mksenzov
 * @param <T> The type of the compared elements in the 'lines'.
 */
public class DeltaComparator implements Comparator<Delta<?>>, Serializable {
    private static final long serialVersionUID = 1L;
    public static final Comparator<Delta<?>> INSTANCE = new DeltaComparator();

    private DeltaComparator() {
    }

    public int compare(final Delta<?> a, final Delta<?> b) {
        final int posA = a.getOriginal().getPosition();
        final int posB = b.getOriginal().getPosition();
        if (posA > posB) {
            return 1;
        } else if (posA < posB) {
            return -1;
        }
        return 0;
    }
}