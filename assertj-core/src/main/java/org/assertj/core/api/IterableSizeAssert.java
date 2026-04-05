/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import org.assertj.core.annotation.CheckReturnValue;

public class IterableSizeAssert<ELEMENT>
    extends AbstractIterableSizeAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public IterableSizeAssert(AbstractIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> originAssert) {
    super(originAssert);
  }

  /**
   * @deprecated use {@link #IterableSizeAssert(AbstractIterableAssert)} instead.
   */
  @Deprecated
  public IterableSizeAssert(AbstractIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> originAssert,
                            @SuppressWarnings("unused") Integer size) {
    this(originAssert);
  }

  @Override
  @CheckReturnValue
  public AbstractIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> returnToIterable() {
    return super.returnToIterable();
  }

}
