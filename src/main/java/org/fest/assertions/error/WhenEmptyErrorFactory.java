/*
 * Created on Sep 26, 2010
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
package org.fest.assertions.error;

import org.fest.assertions.description.Description;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies a group of elements is not empty
 * fails. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class WhenEmptyErrorFactory implements AssertionErrorFactory {

  @VisibleForTesting static final WhenEmptyErrorFactory INSTANCE = new WhenEmptyErrorFactory();

  /**
   * Creates instances of <code>{@link WhenEmptyErrorFactory}</code>.
   * @return an instance of {@code WhenEmptyErrorFactory}.
   */
  public static AssertionErrorFactory errorWhenEmpty() {
    return INSTANCE;
  }

  private WhenEmptyErrorFactory() {}

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies a group of elements is not empty
   * fails. A group of elements can be a collection, an array or a {@code String}
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    String msg = Formatter.instance().formatMessage("%sunexpected empty", d);
    return new AssertionError(msg);
  }

}
