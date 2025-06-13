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
package org.assertj.core.extractor;

import org.assertj.core.api.iterable.ThrowingExtractor;

import java.util.function.Function;

public class ThrowingExtractors {

  /**
   * Provides extractor for extracting {@link java.lang.Object#toString} from any object
   * @return the built {@link ThrowingExtractor}
   */
  public static ThrowingExtractor<Object, String> toStringMethod() {
    return new ToStringThrowingExtractor();
  }

  /**
   * Provides extractor for extracting single field or property from any object using reflection
   * @param fieldOrProperty the name of the field/property to extract
   * @return the built {@link ThrowingExtractor}
   */
  public static ThrowingExtractor<Object, Object> byName(String fieldOrProperty) {
    return new ByNameSingleThrowingExtractor(fieldOrProperty);
  }
}
