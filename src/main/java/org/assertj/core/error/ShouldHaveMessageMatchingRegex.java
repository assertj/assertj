/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} matches given regular
 * expression.
 * 
 * @author Libor Ondrusek
 */
public class ShouldHaveMessageMatchingRegex extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveMessageMatchingRegex(Throwable actual, CharSequence regex) {
    return new ShouldHaveMessageMatchingRegex("%nExpecting message:%n  <%s>%nto match regex:%n  <%s>%nbut did not.",
                                              actual.getMessage(), regex);
  }

  private ShouldHaveMessageMatchingRegex(String format, CharSequence actual, CharSequence regex) {
    super(format, actual, regex);
  }
}
