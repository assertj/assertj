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

/**
 * Strategy for handling assertion errors in non-standard assertion modes.
 * <p>
 * When set on an {@link AbstractAssert}, assertion methods delegate error handling to this handler
 * instead of throwing directly. This enables:
 * <ul>
 *   <li><b>Soft assertions</b>: errors are collected and reported together at the end</li>
 *   <li><b>Assumptions</b>: errors are converted to assumption exceptions (skipping the test)</li>
 * </ul>
 *
 * @since 4.0.0
 */
public interface AssertionErrorHandler {

  /**
   * Handle an assertion error. Implementations may collect it, rethrow it as a different exception type, etc.
   *
   * @param error the assertion error to handle
   */
  void handleError(AssertionError error);

  /**
   * Called when an assertion succeeds.
   */
  void succeeded();
  void skipChainedAssertions();
  boolean mustSkipChainedAssertions();
}
