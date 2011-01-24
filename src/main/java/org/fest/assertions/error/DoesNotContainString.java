/*
 * Created on Dec 26, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code String} contains another
 * {@code String} failed.
 *
 * @author Alex Ruiz
 */
public class DoesNotContainString extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link DoesNotContainString}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessageFactory doesNotContain(Object actual, Object sequence) {
    return new DoesNotContainString("expecting:<%s> to contain:<%s>", actual, sequence);
  }

  /**
   * Creates a new <code>{@link DoesNotContainString}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessageFactory doesNotContainIgnoringCase(String actual, String sequence) {
    return new DoesNotContainString("expecting:<%s> to contain:<%s> (ignoring case)", actual, sequence);
  }

  private DoesNotContainString(String format, Object actual, Object sequence) {
    super(format, actual, sequence);
  }
}
