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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.test;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Dummy spliterator for testing purpose
 *
 * @author William Bakker
 */
public class StringSpliterator implements Spliterator<String> {
  private final int characteristics;

  public StringSpliterator() {
    this(0);
  }

  public StringSpliterator(int characteristics) {
    this.characteristics = characteristics;
  }

  @Override
  public boolean tryAdvance(Consumer<? super String> action) {
    return false;
  }

  @Override
  public Spliterator<String> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return 0;
  }

  @Override
  public int characteristics() {
    return characteristics;
  }
}
