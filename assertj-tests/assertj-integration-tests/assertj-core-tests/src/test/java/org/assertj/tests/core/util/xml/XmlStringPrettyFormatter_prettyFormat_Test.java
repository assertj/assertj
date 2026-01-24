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
package org.assertj.tests.core.util.xml;

import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.xml.XmlStringPrettyFormatter.xmlPrettyFormat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.DefaultLocale;
import org.xml.sax.SAXParseException;

/**
 * @author Joel Costigliola
 */
@DefaultLocale("en")
class XmlStringPrettyFormatter_prettyFormat_Test {

  private final BigDecimal javaVersion = new BigDecimal(System.getProperty("java.specification.version"));

  private final String expected_formatted_xml = javaVersion.compareTo(new BigDecimal("9")) >= 0
      ? """
          <?xml version="1.0" encoding="UTF-8"?><rss version="2.0">
              <channel>
                  <title>Java Tutorials and Examples 1</title>
                  <language>en-us</language>
              </channel>
          </rss>
          """
      : """
          <?xml version="1.0" encoding="UTF-8"?>
          <rss version="2.0">
              <channel>
                  <title>Java Tutorials and Examples 1</title>
                  <language>en-us</language>
              </channel>
          </rss>
          """;

  @Test
  void should_format_input_prettily() {
    // GIVEN
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel><title>Java Tutorials and Examples 1</title><language>en-us</language></channel></rss>";
    // WHEN
    String result = xmlPrettyFormat(xmlString);
    // THEN
    then(result).isEqualTo(expected_formatted_xml);
  }

  @Test
  void should_format_input_without_xml_declaration_prettily() {
    // GIVEN
    String xmlString = "<rss version=\"2.0\"><channel><title>Java Tutorials and Examples 1</title><language>en-us</language></channel></rss>";
    // WHEN
    String result = xmlPrettyFormat(xmlString);
    // THEN
    if (javaVersion.compareTo(new BigDecimal("9")) >= 0) {
      then(result).isEqualTo(expected_formatted_xml.substring("<?xml version='1.0' encoding='UTF-8'?>".length()));
    } else {
      then(result).isEqualTo(expected_formatted_xml.substring("<?xml version='1.0' encoding='UTF-8'?>\n".length()));
    }
  }

  @Test
  void should_format_input_with_space_and_newline_prettily() {
    // GIVEN
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel>  <title>Java Tutorials and Examples 1</title>  \n\n<language>en-us</language>  </channel></rss>";
    // WHEN
    String result = xmlPrettyFormat(xmlString);
    // THEN
    then(result).isEqualTo(expected_formatted_xml);
  }

  @Test
  void should_fail_if_input_is_null() {
    // WHEN
    Exception exception = catchException(() -> xmlPrettyFormat(null));
    // THEN
    then(exception).isInstanceOf(IllegalArgumentException.class)
                   .hasMessageStartingWith("Expecting XML String not to be null");
  }

  @Test
  void should_fail_if_input_is_not_valid() {
    // GIVEN
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel><title>Java Tutorials and Examples 1</title><language>en-us</language></chnel></rss>";
    // WHEN
    Exception exception = catchException(() -> xmlPrettyFormat(xmlString));
    // THEN
    then(exception).isInstanceOf(RuntimeException.class)
                   .hasMessage("Unable to format XML string")
                   .hasRootCauseInstanceOf(SAXParseException.class)
                   .cause()
                   .hasMessageContaining("The element type \"channel\" must be terminated by the matching end-tag \"</channel>\"");
  }

  @ParameterizedTest
  @ValueSource(strings = {
      // Injection into document content
      """
          <?xml version="1.0"?>
          <!DOCTYPE root [
            <!ENTITY xxe SYSTEM "file:///etc/hosts">
          ]>
          <root>&xxe;</root>
          """,
      // Injection during document parsing
      """
          <?xml version="1.0"?>
          <!DOCTYPE root [
            <!ENTITY % xxe SYSTEM "file:///etc/hosts">
            %xxe;
          ]>
          <root>foo</root>
          """
  })
  void should_fail_if_input_contains_doctype_declaration(String input) {
    // WHEN
    Exception exception = catchException(() -> xmlPrettyFormat(input));
    // THEN
    then(exception).isInstanceOf(RuntimeException.class)
                   .hasMessage("Unable to format XML string")
                   .cause()
                   .isInstanceOf(SAXParseException.class)
                   .hasMessageContaining("DOCTYPE is disallowed");
  }

}
