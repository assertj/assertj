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
package org.assertj.core.util.diff.stream;

import java.io.IOException;

@FunctionalInterface
interface CharSequenceSupplier {
  /**
   * Get a {@link CharSequence} for a given interval.
   * @param from begin position (inclusive)
   * @param to end position (exclusive)
   * @return a {@link CharSequence} for the wanted interval.
   * @throws IOException
   */
  CharSequence get(long from, long to) throws IOException;
}
