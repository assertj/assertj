package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;

import java.util.concurrent.Callable;

import org.assertj.core.internal.Failures;

public abstract class AbstractCallableAssert<SELF extends AbstractCallableAssert<SELF, V>, V>
    extends AbstractAssert<SELF, Callable<V>> {

  private V result;
  private Throwable thrown;

  protected AbstractCallableAssert(Callable<V> actual, Class<?> selfType) {
    super(actual, selfType);

    try {
      result = actual.call();
    } catch (Throwable e) {
      thrown = e;
    }
  }

  public SELF doesNotThrowAnyException() {
    if (thrown != null) throw Failures.instance().failure(info, shouldNotHaveThrown(thrown));
    return myself;
  }

  public AbstractObjectAssert<?, V> result() {
    return internalResult();
  }

  public <ASSERT extends AbstractAssert<?, ?>> ASSERT result(InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalResult().asInstanceOf(assertFactory);
  }

  private AbstractObjectAssert<?, V> internalResult() {
    doesNotThrowAnyException(); // FIXME enough? better error message?
    return assertThat(result).withAssertionState(myself);
  }

  public ThrowableAssertAlternative<Throwable> hasThrownException() {
    if (thrown == null) throw Failures.instance().expectedThrowableNotThrown(Throwable.class);
    return new ThrowableAssertAlternative<>(thrown).withAssertionState(myself);
  }

}
