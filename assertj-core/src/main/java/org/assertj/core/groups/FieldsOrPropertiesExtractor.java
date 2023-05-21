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
package org.assertj.core.groups;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Streams.stream;

import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AbstractObjectArrayAssert;

/**
 * Understands how to retrieve fields or values from a collection/array of objects.
 * <p>
 * You just have to give the field/property name or a {@link Function} implementation, a collection/array of objects
 * and it will extract the list of field/values from the given objects.
 *
 * @author Joel Costigliola
 * @author Mateusz Haligowski
 *
 */
public class FieldsOrPropertiesExtractor {

  /**
   * Call {@link #extract(Iterable, Function)} after converting objects to an iterable.
   * <p>
   * Behavior is described in javadoc {@link AbstractObjectArrayAssert#extracting(Function)}
   * @param <F> type of elements to extract a value from
   * @param <T> the extracted value type
   * @param objects the elements to extract a value from
   * @param extractor the extractor function
   * @return the extracted values
   */
  public static <F, T> T[] extract(F[] objects, Function<? super F, T> extractor) {
    checkObjectToExtractFromIsNotNull(objects);
    List<T> result = extract(newArrayList(objects), extractor);
    return toArray(result);
  }

  /**
   * Behavior is described in {@link AbstractIterableAssert#extracting(Function)}
   * @param <F> type of elements to extract a value from
   * @param <T> the extracted value type
   * @param objects the elements to extract a value from
   * @param extractor the extractor function
   * @return the extracted values
   */
  public static <F, T> List<T> extract(Iterable<? extends F> objects, Function<? super F, T> extractor) {
    checkObjectToExtractFromIsNotNull(objects);
    return stream(objects).map(extractor).collect(toList());
  }

  private static void checkObjectToExtractFromIsNotNull(Object object) {
    if (object == null) throw new AssertionError("Expecting actual not to be null");
  }

}
