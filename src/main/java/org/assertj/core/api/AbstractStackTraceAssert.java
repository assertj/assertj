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
 * Base class for all implementations of assertions for
 * {@link StackTraceElement[] stack trace arrays} of
 * {@link StackTraceElement stack trace elements}.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a
 *               href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java
 *               Generics to simplify fluent API implementation</a>&quot; for more details.
 * @author Ashley Scopes
 */
public abstract class AbstractStackTraceAssert<SELF extends AbstractStackTraceAssert<SELF, ELEMENT_ASSERT>,
                                               ELEMENT_ASSERT extends AbstractStackTraceElementAssert<ELEMENT_ASSERT>>
  extends AbstractAssert<SELF, StackTraceElement[]> {

  /**
   * Initialize this assertion.
   *
   * @param actual   the stack trace array to perform assertions upon.
   * @param selfType the type of the implementation of this class.
   */
  protected AbstractStackTraceAssert(StackTraceElement[] actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Create an assertion for a {@link StackTraceElement} and return it.
   *
   * @param stackTraceElement the stack trace element to create an assertion for.
   * @return the created assertion.
   */
  protected abstract ELEMENT_ASSERT newStackTraceElementAssert(StackTraceElement stackTraceElement);
}
