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
package org.assertj.core.internal.iterables;

import java.util.Iterator;
import java.util.List;

class SinglyIterableFactory {

  static Iterable<String> createSinglyIterable(final List<String> values) {
    // can't use Iterable<> for anonymous class in java 8
    return new Iterable<String>() {
      private boolean isIteratorCreated = false;

      @Override
      public Iterator<String> iterator() {
        if (isIteratorCreated) throw new IllegalArgumentException("Cannot create two iterators on a singly-iterable sequence");
        isIteratorCreated = true;
        return new Iterator<String>() {
          private final Iterator<String> listIterator = values.iterator();

          @Override
          public boolean hasNext() {
            return listIterator.hasNext();
          }

          @Override
          public String next() {
            return listIterator.next();
          }
        };
      }
    };
  }

}
