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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Iterator;

/**
 * Assertion methods for {@link Iterator}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Iterator)}</code>.
 * </p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Stephan Windmüller
 */
public class IteratorAssert<ELEMENT> extends AbstractIteratorAssert<IteratorAssert<ELEMENT>, ELEMENT> {

  public IteratorAssert(Iterator<? extends ELEMENT> actual) {
    super(actual, IteratorAssert.class);
  }

}
