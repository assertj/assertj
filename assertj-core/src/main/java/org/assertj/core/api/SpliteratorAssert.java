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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Spliterator;

/**
 * Assertion methods for {@code Spliterator}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Spliterator)}</code>.
 *
 * @author William Bakker
 *
 * @param <ELEMENT> the type of elements.
 */
public class SpliteratorAssert<ELEMENT> extends AbstractSpliteratorAssert<SpliteratorAssert<ELEMENT>, ELEMENT> {

  public static <ELEMENT> SpliteratorAssert<ELEMENT> assertThatSpliterator(Spliterator<ELEMENT> actual) {
    return new SpliteratorAssert<>(actual);
  }

  /**
   * Creates a new <code>{@link SpliteratorAssert}</code>.
   *
   * @param actual the actual value to verify
   */
  protected SpliteratorAssert(Spliterator<ELEMENT> actual) {
    super(actual, SpliteratorAssert.class);
  }
}
