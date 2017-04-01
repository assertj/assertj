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

@FunctionalInterface
public interface ThrowingExtractor<F, T, E extends Exception> extends Extractor<F,T> {

  @Override
  default T extract(final F input) {
    try {
      return extractThrows(input);
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  T extractThrows(F input) throws E;
}
