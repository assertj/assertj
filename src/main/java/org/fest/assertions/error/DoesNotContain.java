/*
 * Created on Oct 18, 2010
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

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains a given set of
 * values failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class DoesNotContain extends BasicErrorMessage {

  /**
   * Creates a new </code>{@link DoesNotContain}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be in {@code actual}.
   * @param notFound the values in {@code expected} not found in {@code actual}.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage doesNotContain(Object actual, Object expected, Object notFound) {
    return new DoesNotContain(actual, expected, notFound);
  }

  private DoesNotContain(Object actual, Object expected, Object notFound) {
    super("%sexpecting:<%s> to contain:<%s> but could not find:<%s>", actual, expected, notFound);
  }
}
