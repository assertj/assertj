/*
 * Created on Nov 25, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.internal.ArrayWrapperList.wrap;

import org.fest.assertions.core.AssertionInfo;

/**
 * Assertions on arrays (both Object and primitives.) This class is intended for internal use only, since it lacks
 * type-safety (arrays are passed as {@code Object} to handle both types of arrays,) and it uses reflection under the
 * covers. The purpose of this class is to avoid code duplication, at the price of performance.
 *
 * @author Alex Ruiz
 */
final class Arrays {

  private final Collections collections;

  Arrays(Failures failures) {
    collections = new Collections(failures);
  }

  void assertNullOrEmpty(AssertionInfo info, Object actual) {
    collections.assertNullOrEmpty(info, wrap(actual));
  }

  void assertEmpty(AssertionInfo info, Object actual) {
    collections.assertEmpty(info, wrap(actual));
  }

  void assertNotEmpty(AssertionInfo info, Object actual) {
    collections.assertNotEmpty(info, wrap(actual));
  }

  void assertHasSize(AssertionInfo info, Object actual, int expectedSize) {
    collections.assertHasSize(info, wrap(actual), expectedSize);
  }

  void assertContains(AssertionInfo info, Object actual, Object[] values) {
    collections.assertContains(info, wrap(actual), values);
  }

  void assertContainsOnly(AssertionInfo info, Object actual, Object[] values) {
    collections.assertContainsOnly(info, wrap(actual), values);
  }

  public void assertDoesNotContain(AssertionInfo info, Object actual, Object[] values) {
    collections.assertDoesNotContain(info, wrap(actual), values);
  }

  void assertDoesNotHaveDuplicates(AssertionInfo info, Object actual) {
    collections.assertDoesNotHaveDuplicates(info, wrap(actual));
  }
}
