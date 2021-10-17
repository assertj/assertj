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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.presentation;

import org.assertj.core.test.Xml;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class StandardRepresentation_toStringOf_multiline_format_Test {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  void should_display_unix_multiline_string_properly_formatted() {
    // GIVEN
    Xml xml = Xml.newUnixXml("A");
    // WHEN
    String stringOf = STANDARD_REPRESENTATION.toStringOf(xml);
    // THEN
    then(stringOf).isEqualTo("\n" +
                             "  <xml>\n" +
                             "    <value>A</value>\n" +
                             "  </xml>");
  }

  @Test
  void should_display_mac_multiline_string_properly_formatted() {
    // GIVEN
    Xml xml = Xml.newMacXml("A");
    // WHEN
    String stringOf = STANDARD_REPRESENTATION.toStringOf(xml);
    // THEN
    then(stringOf).isEqualTo("\r" +
                             "  <xml>\r" +
                             "    <value>A</value>\r" +
                             "  </xml>");
  }

  @Test
  void should_display_dos_multiline_string_properly_formatted() {
    // GIVEN
    Xml xml = Xml.newDosXml("A");
    // WHEN
    String stringOf = STANDARD_REPRESENTATION.toStringOf(xml);
    // THEN
    then(stringOf).isEqualTo("\r\n" +
                             "  <xml>\r\n" +
                             "    <value>A</value>\r\n" +
                             "  </xml>");
  }
}
