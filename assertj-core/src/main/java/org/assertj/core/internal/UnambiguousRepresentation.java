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
package org.assertj.core.internal;

import java.util.Objects;

import org.assertj.core.presentation.Representation;

/**
 * Utility class around {@link Representation} to provide the {@link Representation#toStringOf(Object) toStringOf}
 * representations of {@code actual} and {@code expected} when they are different, and their
 * {@link Representation#unambiguousToStringOf(Object) unambiguousToStringOf} representations if not.
 */
public class UnambiguousRepresentation {

  private final String actual;

  private final String expected;

  public UnambiguousRepresentation(Representation representation, Object actual, Object expected) {
    String actualRepresentation = representation.toStringOf(actual);
    String expectedRepresentation = representation.toStringOf(expected);

    boolean sameRepresentation = Objects.equals(actualRepresentation, expectedRepresentation);
    this.actual = sameRepresentation
        ? representation.unambiguousToStringOf(actual)
        : actualRepresentation;
    this.expected = sameRepresentation
        ? representation.unambiguousToStringOf(expected)
        : expectedRepresentation;
  }

  /**
   * Provide a representation of {@code actual} guaranteed to be different
   * from {@link #getExpected()}.
   * @return a suitable representation of the {@code actual} object given at
   * construction time.
   */
  public String getActual() {
    return actual;
  }

  /**
   * Provide a representation of {@code expected} guaranteed to be different
   * from {@link #getActual()}.
   * @return a suitable representation of the {@code expected} object given at
   * construction time.
   */
  public String getExpected() {
    return expected;
  }

}
