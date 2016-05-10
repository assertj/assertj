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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

public class IterableSizeAssert<T> extends AbstractIterableSizeAssert<IterableAssert<T>, Iterable<? extends T>, T> {

  private AbstractIterableAssert<IterableAssert<T>, Iterable<? extends T>, T> source;

  public IterableSizeAssert(AbstractIterableAssert<IterableAssert<T>, Iterable<? extends T>, T> source, Integer i) {
    super(i, IterableSizeAssert.class);
    this.source = source;
  }

  public AbstractIterableAssert<IterableAssert<T>, Iterable<? extends T>, T> returnToIterable() {
    return source;
  }
}
