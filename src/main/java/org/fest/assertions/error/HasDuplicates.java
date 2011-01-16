/*
 * Created on Oct 17, 2010
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
 * Creates an error message indicating that an assertion that verifies a group of elements is does
 * not have duplicates failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class HasDuplicates extends BasicErrorMessage {

  /**
   * Creates a new <code>{@link HasDuplicates}</code>.
   * @param actual the actual value in the failed assertion.
   * @param duplicates the duplicate values found in {@code actual}.
   * @return an instance of {@code ErrorMessage}.
   */
  public static ErrorMessage hasDuplicates(Object actual, Object duplicates) {
    return new HasDuplicates(actual, duplicates);
  }

  private HasDuplicates(Object actual, Object duplicates) {
    super("found duplicate(s):<%s> in:<%s>", duplicates, actual);
  }
}
