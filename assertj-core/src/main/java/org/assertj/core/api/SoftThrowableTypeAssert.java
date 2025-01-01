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

import org.assertj.core.description.Description;
import org.assertj.core.util.CheckReturnValue;

/**
 * ThrowableTypeAssert for soft assertions.
 * 
 * @since 3.23.0
 */
public class SoftThrowableTypeAssert<T extends Throwable> extends ThrowableTypeAssert<T> {

  private final SoftAssertionsProvider softAssertionsProvider;

  /**
   * Default constructor.
   *
   * @param throwableType class representing the target (expected) exception
   * @param softAssertionsProvider the soft assertion instance used later on to proxy {@link ThrowableAssert}
   */
  public SoftThrowableTypeAssert(final Class<? extends T> throwableType, SoftAssertionsProvider softAssertionsProvider) {
    super(throwableType);
    this.softAssertionsProvider = softAssertionsProvider;
  }

  @Override
  protected ThrowableAssertAlternative<T> buildThrowableTypeAssert(T throwable) {
    return new SoftThrowableAssertAlternative<>(throwable, softAssertionsProvider);
  }

  @Override
  protected void checkThrowableType(Throwable throwable) {
    try {
      super.checkThrowableType(throwable);
    } catch (AssertionError error) {
      this.softAssertionsProvider.collectAssertionError(error);
    }
  }

  @Override
  @CheckReturnValue
  public SoftThrowableTypeAssert<T> describedAs(Description description) {
    this.description = description;
    return this;
  }
}
