package org.assertj.core.error;

import java.util.Set;

/**
 * Creates an error message indicating that an assertion that verifies a map does not contain keys.
 *
 * @author dorzey
 */
public class ShouldNotContainKeys  extends BasicErrorMessageFactory {

    /**
     * Creates a new </code>{@link ShouldNotContainKeys}</code>.
     *
     * @param actual the actual value in the failed assertion.
     * @return the created {@code ErrorMessageFactory}.
     */
    public static <K> ErrorMessageFactory shouldNotContainKeys(Object actual, Set<K> keys) {
        if (keys.size() == 1) return new ShouldNotContainKeys(actual, keys.iterator().next());
        return new ShouldNotContainKeys(actual, keys);
    }

    private <K> ShouldNotContainKeys(Object actual, Set<K> key) {
        super("\nExpecting:\n <%s>\nnot to contain keys:\n <%s>", actual, key);
    }

    private <K> ShouldNotContainKeys(Object actual, K key) {
        super("\nExpecting:\n <%s>\nnot to contain key:\n <%s>", actual, key);
    }
}

