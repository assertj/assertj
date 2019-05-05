package org.assertj.core.api;

import static java.util.Objects.requireNonNull;

/**
 * {@link AssertFactory} decorator which casts the input value to the given type before invoking the decorated {@link AssertFactory}.
 *
 * @param <T>      the type to use for the cast.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 *
 * @since 3.13.0
 */
public class InstanceOfAssertFactory<T, ASSERT extends Assert<?, ?>> implements AssertFactory<Object, ASSERT> {

  private final Class<T> type;
  private final AssertFactory<T, ASSERT> assertFactory;

  /**
   * Instantiates a new {@code InstanceOfAssertFactory}.
   *
   * @param type          the {@code Class} instance of the given type.
   * @param assertFactory the {@code AssertFactory} to decorate.
   */
  public InstanceOfAssertFactory(Class<T> type, AssertFactory<T, ASSERT> assertFactory) {
    this.type = requireNonNull(type, "'type' cannot be null");
    this.assertFactory = requireNonNull(assertFactory, "'assertFactory' cannot be null");
  }

  /** {@inheritDoc} */
  @Override
  public ASSERT createAssert(Object value) {
    Assertions.assertThat(value).isInstanceOf(type);
    return assertFactory.createAssert(type.cast(value));
  }

}
