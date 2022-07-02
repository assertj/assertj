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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Implementation of a set of assertions that can be performed on
 * {@link StackTraceElement[] stack trace element arrays} types.
 *
 * @author Ashley Scopes
 */
public class StackTraceAssert extends AbstractStackTraceAssert<StackTraceAssert, StackTraceElementAssert> {

  /**
   * Initialize these assertions.
   *
   * @param actual the stack trace element array to perform assertions on.
   */
  protected StackTraceAssert(StackTraceElement[] actual) {
    super(actual, StackTraceAssert.class);
  }

  @Override
  protected StackTraceAssert newObjectArrayAssert(StackTraceElement[] array) {
    return new StackTraceAssert(array);
  }

  @Override
  protected StackTraceElementAssert toAssert(StackTraceElement value, String description) {
    return new StackTraceElementAssert(value).describedAs(description);
  }
}
