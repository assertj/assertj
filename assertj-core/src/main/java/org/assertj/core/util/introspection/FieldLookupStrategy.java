package org.assertj.core.util.introspection;

import java.lang.reflect.Field;

@FunctionalInterface
interface FieldLookupStrategy {
 
  Field findByName(Class<?> acls, String fieldName) throws NoSuchFieldException;

  /**
   * Returns a human-readable description of the strategy to be used in error messages.
   * <p>
   * Default implementation returns {@code this.getClass().getSimpleName()}.
   *
   * @return a description of the strategy
   */
  default String getDescription() {
    return this.getClass().getSimpleName();
  }
}
