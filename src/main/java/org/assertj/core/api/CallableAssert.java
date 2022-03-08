package org.assertj.core.api;

import java.util.concurrent.Callable;

public class CallableAssert<V> extends AbstractCallableAssert<CallableAssert<V>, V> {

  public CallableAssert(Callable<V> actual) {
    super(actual, CallableAssert.class);
  }

}
