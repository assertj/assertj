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
package org.assertj.core.error;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class ShouldBeMarked extends BasicErrorMessageFactory {

  private static final String EXPECTING_TO_BE_MARKED = "%nExpecting <%s> to be a marked but was not";
  private static final String EXPECTING_NOT_TO_BE_MARKED = "%nExpecting <%s> not to be a marked but was";

  public static ErrorMessageFactory shouldBeMarked(AtomicMarkableReference<?> actual) {
    return new ShouldBeMarked(actual, true);
  }

  public static ErrorMessageFactory shouldNotBeMarked(AtomicMarkableReference<?> actual) {
    return new ShouldBeMarked(actual, false);
  }
  
  private ShouldBeMarked(AtomicMarkableReference<?> actual, boolean marked) {
    super(marked ? EXPECTING_TO_BE_MARKED : EXPECTING_NOT_TO_BE_MARKED, actual);
  }
}
