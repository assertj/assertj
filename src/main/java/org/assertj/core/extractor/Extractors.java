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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.extractor;

import static java.lang.String.format;

import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Strings;

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
   * @return the built {@link Function}
   */
  public static Function<Object, String> toStringMethod() {
    return new ToStringExtractor();
  }

  /**
   * Provides extractor for extracting single field or property from any object using reflection
   * @param fieldOrProperty the name of the field/property to extract
   * @return the built {@link Function}
   */
  public static Function<Object, Object> byName(String fieldOrProperty) {
    return new ByNameSingleExtractor(fieldOrProperty);
  }

  /**
   * Provides extractor for extracting multiple fields or properties from any object using reflection
   * @param fieldsOrProperties the name of the fields/properties to extract
   * @return the built {@link Function}
   */
  public static Function<Object, Tuple> byName(String... fieldsOrProperties) {
    return new ByNameMultipleExtractor(fieldsOrProperties);
  }

  /**
   * Provides extractor for extracting values by method name from any object using reflection
   * @param methodName the name of the method to execute
   * @return the built {@link Function}
   */
  public static Function<Object, Object> resultOf(String methodName) {
    return new ResultOfExtractor(methodName);
  }

  public static String extractedDescriptionOf(String... itemsDescription) {
    return format("Extracted: %s", Strings.join(itemsDescription).with(", "));
  }

  public static String extractedDescriptionOf(Object... items) {
    String[] itemsDescription = Stream.of(items).map(Object::toString).toArray(String[]::new);
    return extractedDescriptionOf(itemsDescription);
  }

  public static String extractedDescriptionOfMethod(String method) {
    return format("Extracted: result of %s()", method);
  }

}
