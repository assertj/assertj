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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

public class WithThrowable {
  private final Throwable throwable;

  WithThrowable(Throwable exception) {
    this.throwable = exception;
  }

  /**
   * Checks that the underlying throwable is of the given type and returns a {@link ThrowableAssertAlternative} to chain
   * further assertions on the underlying throwable.
   * @param type the expected {@link Throwable} type
   * @return a {@link ThrowableAssertAlternative} built with underlying throwable.
   */
  public ThrowableAssertAlternative<?> withThrowableOfType(Class<? extends Throwable> type) {
    return new ThrowableAssertAlternative<>(throwable).isInstanceOf(type);
  }

}