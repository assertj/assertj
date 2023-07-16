package org.assertj.core.presentation;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Converts elements of one list to a different type on demand.
 *
 * @param <F> the type to convert from
 * @param <T> the type to convert to
 */
final class TransformingList<F, T> extends AbstractList<T> {
  /** The list to transform. */
  private final List<? extends F> source;

  /** Converts elements to the new type. */
  private final Function<? super F, ? extends T> transform;

  /**
   * Creates a new {@code TransformingList}.
   *
   * @param source the list to transform
   * @param transform transforms elements to the output type
   */
  TransformingList(final List<? extends F> source, final Function<? super F, ? extends T> transform) {
    this.source = Objects.requireNonNull(source, "source");
    this.transform = Objects.requireNonNull(transform, "transform");
  }

  @Override
  public T get(final int index) {
    return transform.apply(source.get(index));
  }

  @Override
  public int size() {
    return source.size();
  }
}
