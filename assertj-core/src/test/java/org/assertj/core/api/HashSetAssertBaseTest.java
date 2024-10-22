package org.assertj.core.api;

import org.assertj.core.internal.Iterables;

import java.util.HashSet;

import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public abstract class HashSetAssertBaseTest extends BaseTestTemplate<HashSetAssert<Object>, HashSet<? extends Object>> {
  private Iterables iterables;
  private Iterables hashSets;
  private Object[] values = {"Yoda", "Luke"};

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    iterables = mock(Iterables.class);
    hashSets = mock(Iterables.class);
    assertions.iterables = iterables;
    assertions.hashSets = hashSets;
  }

  @Override
  protected HashSetAssert<Object> create_assertions() {
    return new HashSetAssert<>(newHashSet());
  }

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.contains(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContains(getInfo(assertions), getActual(assertions), values);
    verify(hashSets).assertContains(getInfo(assertions), getActual(assertions), values);
  }
}
