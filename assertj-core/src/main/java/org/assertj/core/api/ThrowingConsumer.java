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

import java.util.function.Consumer;

/**
 * {@link Consumer} that deals with checked exceptions by rethrowing them as {@link RuntimeException}.
 * <p>
 * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are,
 * while any other {@link Throwable} is wrapped in a {@link RuntimeException} and rethrown.
 *
 * @param <T> consumed type
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

  @Override
  default void accept(final T input) {
    try {
      acceptThrows(input);
    } catch (final RuntimeException | AssertionError e) {
      throw e;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  void acceptThrows(T input) throws Throwable;

}
