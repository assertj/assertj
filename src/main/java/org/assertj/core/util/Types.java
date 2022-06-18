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
 * Copyright 2012-2022 the original author or authors.
 */
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
