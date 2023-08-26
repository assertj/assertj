/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Visits the major types of collections from {@code java.util}. */
public interface CollectionVisitor<T> {
  /** Wraps an exception thrown by the targe collection. */
  public final class TargetException extends RuntimeException {
    /** The original exception, never {@code null}. */
    private final RuntimeException exception;

    /**
     * Creates a new {@link TargetException}.
     *
     * @param message describes the operation that caused the problem
     * @param exception the original exception
     * @throws NullPointerException if either argument is {@code null}
     */
    public TargetException(final String message, final RuntimeException exception) {
      super(message, exception);
      Objects.requireNonNull(message, "message can't be null");
      Objects.requireNonNull(exception, "exception can't be null");

      this.exception = exception;
    }

    /**
     * Gets the original exception. This returns the same value as {@link Exception#getCause()}.
     *
     * @return the original exception
     */
    public RuntimeException getException() {
      return exception;
    }
  }

  /**
   * Visits a collection.
   *
   * @param target the collection to visit
   * @return the result of the visit
   * @throws TargetException if the target threw a runtime exception
   */
  T visitCollection(Collection<?> target);

  /**
   * Visits a list.
   *
   * @param target the list to visit
   * @return the result of the visit
   * @throws TargetException if the target threw a runtime exception
   */
  T visitList(List<?> target);

  /**
   * Visits a set.
   *
   * @param target the set to visit
   * @return the result of the visit
   * @throws TargetException if the target threw a runtime exception
   */
  T visitSet(Set<?> target);

  /**
   * Visits a map.
   *
   * @param target the map to visit
   * @return the result of the visit
   * @throws TargetException if the target threw a runtime exception
   */
  T visitMap(Map<?, ?> target);
}
