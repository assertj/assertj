package org.assertj.core.util;

import static java.util.Objects.requireNonNull;

import org.assertj.core.annotations.Unsafe;

/**
 * Type handling utilities.
 *
 * @author Ashley Scopes
 */
public class Types {

  private Types() {
    // Static-only API.
  }

  /**
   * Cast a class to the given type.
   *
   * <p>This operation is unsafe, it discards any ability for the compiler to detect mistakes. Only
   * use it in places where it is required and the cast side effects are well-known and handled.
   *
   * @param rawType the raw type to cast.
   * @return the cast type.
   */
  @SuppressWarnings("unchecked")
  @Unsafe
  public static <T> Class<T> castClass(Class<?> rawType) {
    return (Class<T>) requireNonNull(rawType, "cannot cast a null type");
  }
}
