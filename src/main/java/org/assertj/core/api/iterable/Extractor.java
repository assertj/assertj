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
package org.assertj.core.api.iterable;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssert;

/**
 * Function converting an element to another element. Used in {@link ListAssert#extracting(Extractor)},
 * {@link ObjectArrayAssert#extracting(Extractor)} and {@link AtomicReferenceArrayAssert#extracting(Extractor)}.
 * 
 * @author Mateusz Haligowski
 *
 * @param <F> type of element from which the conversion happens
 * @param <T> target element type
 */
@FunctionalInterface
public interface Extractor<F, T> {
  T extract(F input);
}
