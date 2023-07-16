package org.assertj.core.presentation;

import org.assertj.core.util.Arrays;
import org.assertj.core.util.Preconditions;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Objects;

/**
 * Provides a read-only view of an array as a list.
 *
 * <p>This is different from {@link java.util.ArrayList} because the array may be an array of primitives. Arrays of non-primitives
 * also work, but {@link java.util.ArrayList} would probably be a better choice for these arrays.
 */
final class PrimitiveArrayList extends AbstractList<Object> {
  /** The array to provide a view of. */
  private final Object array;

  /**
   * Creates a new {@link PrimitiveArrayList}.
   *
   * @param array primitive or object array
   */
  PrimitiveArrayList(final Object array) {
    Objects.requireNonNull(array, "array must not be null");
    Preconditions.checkArgument(Arrays.isObjectArray(array) || Arrays.isArrayTypePrimitive(array),
                                "input data must be an array type");

    this.array = array;
  }

  @Override
  public Object get(final int index) {
    return Array.get(array, index);
  }

  @Override
  public int size() {
    return Array.getLength(array);
  }
}
