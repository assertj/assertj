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
package org.assertj.tests.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.junit.jupiter.api.Test;

class BasicErrorMessageFactory_unquotedString_Test {

  @Test
  void should_implement_toString() {
    then(unquotedString("some value")).hasToString("some value");
  }

  @Test
  void StandardRepresentation_should_not_quote_unquoted_String() {
    // GIVEN
    CharSequence unquotedString = unquotedString("Hello");
    // WHEN
    String unquotedStringStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(unquotedString);
    // THEN
    then(unquotedStringStandardRepresentation).isEqualTo("Hello");
  }

  private static CharSequence unquotedString(String string) {
    return UnquotedStringAccessor.unquoted(string);
  }

  // unquotedString is protected static, so only a subclass can call it from this package
  private static class UnquotedStringAccessor extends BasicErrorMessageFactory {

    private UnquotedStringAccessor() {
      super("");
    }

    static CharSequence unquoted(String string) {
      return unquotedString(string);
    }
  }
}
