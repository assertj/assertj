package org.assertj.core.api;

import org.assertj.core.internal.Iterators;
import org.assertj.core.util.VisibleForTesting;

import java.util.Iterator;

public abstract class AbstractIteratorAssert<SELF extends AbstractIteratorAssert<SELF>> extends AbstractAssert<SELF, Iterator<Object>> {

  @VisibleForTesting
  Iterators iterators = Iterators.instance();

  public AbstractIteratorAssert(Iterator actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public SELF hasNext() {
    iterators.assertHasNext(info, actual);
    return myself;
  }

  public SELF isExhausted() {
    iterators.assertIsExhausted(info, actual);
    return myself;
  }

}
