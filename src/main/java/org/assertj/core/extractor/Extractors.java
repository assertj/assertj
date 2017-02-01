/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.extractor;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;

/**
 * Extractors factory, providing convenient methods of creating common extractors.
 * <p>
 * 
 * For example:
 * <pre><code class='java'> assertThat(objectsList).extracting(toStringMethod()).contains("toString 1", "toString 2");
 * assertThat(objectsList).extracting(byName("field")).contains("someResult1", "someResult2");</code></pre>
 * 
 * @author Mateusz Haligowski
 *
 */
public class Extractors {
  /**
   * Provides extractor for extracting {@link java.lang.Object#toString} from any object
   */
  public static Extractor<Object, String> toStringMethod() {
    return new ToStringExtractor();
  }
  
  /**
   * Provides extractor for extracting single field or property from any object using reflection
   */
  public static <F> Extractor<F, Object> byName(String fieldOrProperty) {
    return new ByNameSingleExtractor<>(fieldOrProperty);
  }
  
  /**
   * Provides extractor for extracting multiple fields or properties from any object using reflection
   */
  public static <F> Extractor<F, Tuple> byName(String... fieldsOrProperties) {
    return new ByNameMultipleExtractor<>(fieldsOrProperties);
  }

  /**
   * Provides extractor for extracting values by method name from any object using reflection
   */
  public static <F> Extractor<F, Object> resultOf(String methodName) {
    return new ResultOfExtractor<>(methodName);
  }
  
}
