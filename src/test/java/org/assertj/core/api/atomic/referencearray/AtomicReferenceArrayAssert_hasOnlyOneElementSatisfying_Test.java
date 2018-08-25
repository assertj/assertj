package org.assertj.core.api.atomic.referencearray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AtomicReferenceArrayAssert_hasOnlyOneElementSatisfying_Test extends AtomicReferenceArrayAssertBaseTest {

  @Mock
  private Consumer<Object> consumer;

  @BeforeEach
  void beforeEach() {
    initMocks(this);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> create_assertions() {
    return new AtomicReferenceArrayAssert<>(atomicArrayOf(new Object()));
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.hasOnlyOneElementSatisfying(consumer);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertHasOnlyOneElementSatisfying(info(), newArrayList(internalArray()), consumer);
  }
}
