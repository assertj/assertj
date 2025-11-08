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
package org.assertj.core.api;

public class SuppressedExceptionsAssert<INITIAL extends AbstractThrowableAssert<INITIAL, T>, T extends Throwable>
    extends AbstractSuppressedExceptionsAssert<INITIAL, T> {

  private final AbstractThrowableAssert<INITIAL, T> initialThrowableAssert;

  public SuppressedExceptionsAssert(AbstractThrowableAssert<INITIAL, T> initialThrowableAssert,
                                    Throwable[] suppressedExceptions) {
    super(suppressedExceptions, SuppressedExceptionsAssert.class);
    this.initialThrowableAssert = initialThrowableAssert;
  }

  @Override
  protected SuppressedExceptionsAssert<INITIAL, T> newObjectArrayAssert(Throwable[] array) {
    return new SuppressedExceptionsAssert<>(initialThrowableAssert, array);
  }

  @Override
  public AbstractThrowableAssert<INITIAL, T> returnToInitialThrowable() {
    return initialThrowableAssert;
  }
}
