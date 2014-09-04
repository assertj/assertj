package org.assertj.core.api.iterable;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectArrayAssert;

/**
 * Function converting an element to another element. Used in {@link ListAssert#extracting(Extractor)} and
 * {@link ObjectArrayAssert#extracting(Extractor)}.
 * 
 * @author Mateusz Haligowski
 *
 * @param <F> type of element from which the conversion happens
 * @param <T> target element type
 */
public interface Extractor<F, T> {
  T extract(F input);
}
