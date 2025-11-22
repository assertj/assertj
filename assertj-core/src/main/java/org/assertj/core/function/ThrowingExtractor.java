/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.function;

import java.util.function.Function;

import org.assertj.core.util.introspection.PropertyOrFieldSupport;

/**
 * Function converting an element to another element.
 *
 * @param <T> type of element from which the conversion happens
 * @param <R> target element type
 * @see org.assertj.core.api.AtomicReferenceArrayAssert#extracting(ThrowingExtractor)
 * @see org.assertj.core.api.ListAssert#extracting(ThrowingExtractor)
 * @see org.assertj.core.api.ObjectArrayAssert#extracting(ThrowingExtractor)
 */
@FunctionalInterface
public interface ThrowingExtractor<T, R> extends Function<T, R> {

  R extractThrows(T input) throws Exception;

  @Override
  default R apply(final T input) {
    try {
      return extractThrows(input);
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Provides extractor for extracting {@link Object#toString} from any object
   *
   * @return the built {@link ThrowingExtractor}
   */
  static ThrowingExtractor<Object, String> toStringMethod() {
    return Object::toString;
  }

  /**
   * Provides extractor for extracting single field or property from any object using reflection
   *
   * @param fieldOrProperty the name of the field/property to extract
   * @return the built {@link ThrowingExtractor}
   */
  static ThrowingExtractor<Object, Object> byName(String fieldOrProperty) {
    return input -> PropertyOrFieldSupport.EXTRACTION.getValueOf(fieldOrProperty, input);
  }

}
