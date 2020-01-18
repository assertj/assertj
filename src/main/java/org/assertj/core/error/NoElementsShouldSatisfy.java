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
package org.assertj.core.error;

public class NoElementsShouldSatisfy extends BasicErrorMessageFactory {

  public static ErrorMessageFactory noElementsShouldSatisfy(Object actual, Object faultyElement) {
    return new NoElementsShouldSatisfy(actual, faultyElement);
  }

  private NoElementsShouldSatisfy(Object actual, Object faultyElement) {
    super("%n" +
          "Expecting no elements of:%n" +
          "  <%s>%n" +
          "to satisfy the given assertions requirements but these elements did:%n" +
          "  <%s>",
          actual, faultyElement);
  }

}
