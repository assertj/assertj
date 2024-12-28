package org.assertj.core.api;

import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Mockito.mock;

import java.util.HashSet;

import org.assertj.core.internal.Iterables;

public abstract class HashSetAssertBaseTest extends BaseTestTemplate<HashSetAssert<Object>, HashSet<? extends Object>> {
  protected final Object[] someValues = { "Yoda", "Luke" };
  protected Iterables iterables;

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    iterables = mock(Iterables.class);
    assertions.iterables = iterables;
  }

  @Override
  protected HashSetAssert<Object> create_assertions() {
    return new HashSetAssert<>(newHashSet());
  }
}
