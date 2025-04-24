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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.util.Objects.requireNonNull;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

/**
 * Converts elements of one list to a different type on demand.
 *
 * @param <FROM> the type to convert from
 * @param <TO> the type to convert to
 */
final class TransformingList<FROM, TO> extends AbstractList<TO> {
  /** The list to transform. */
  private final List<? extends FROM> source;

  /** Converts elements to the new type. */
  private final Function<? super FROM, ? extends TO> transform;

  /**
   * Creates a new {@code TransformingList}.
   *
   * @param source the list to transform
   * @param transform transforms elements to the output type
   */
  TransformingList(final List<? extends FROM> source, final Function<? super FROM, ? extends TO> transform) {
    this.source = requireNonNull(source, "source list");
    this.transform = requireNonNull(transform, "transform function");
  }

  @Override
  public TO get(final int index) {
    return transform.apply(source.get(index));
  }

  @Override
  public int size() {
    return source.size();
  }
}
