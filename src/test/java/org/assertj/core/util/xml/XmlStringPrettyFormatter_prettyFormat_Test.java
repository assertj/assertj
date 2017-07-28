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
package org.assertj.core.util.xml;

import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.xml.XmlStringPrettyFormatter.xmlPrettyFormat;

import java.util.Locale;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xml.sax.SAXParseException;

/**
 * Tests for <code>{@link XmlStringPrettyFormatter#xmlPrettyFormat(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class XmlStringPrettyFormatter_prettyFormat_Test {

  @Rule
  public ExpectedException thrown = none();

  private String expected_formatted_xml;

  @Before
  public void before() {
    // Set locale to be able to check exception message in English.
    Locale.setDefault(ENGLISH);
    if (System.getProperty("java.specification.version").equals("9")) {
      // seems to be a bug in java 9
      expected_formatted_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\">\n"
                               + "    <channel>\n"
                               + "        <title>Java Tutorials and Examples 1</title>\n"
                               + "        <language>en-us</language>\n"
                               + "    </channel>\n"
                               + "</rss>\n";
    } else {
      expected_formatted_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                               + "<rss version=\"2.0\">\n"
                               + "    <channel>\n"
                               + "        <title>Java Tutorials and Examples 1</title>\n"
                               + "        <language>en-us</language>\n"
                               + "    </channel>\n"
                               + "</rss>\n";
    }
  }

  @Test
  public void should_format_xml_string_prettily() {
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel><title>Java Tutorials and Examples 1</title><language>en-us</language></channel></rss>";
    assertThat(xmlPrettyFormat(xmlString)).isEqualTo(expected_formatted_xml);
  }

  @Test
  public void should_format_xml_string_without_xml_declaration_prettily() {
    String xmlString = "<rss version=\"2.0\"><channel><title>Java Tutorials and Examples 1</title><language>en-us</language></channel></rss>";
    if (System.getProperty("java.specification.version").equals("9")) {
      assertThat(xmlPrettyFormat(xmlString)).isEqualTo(expected_formatted_xml.substring("<?xml version='1.0' encoding='UTF-8'?>".length()));
    } else {
      assertThat(xmlPrettyFormat(xmlString)).isEqualTo(expected_formatted_xml.substring("<?xml version='1.0' encoding='UTF-8'?>\n".length()));
    }
  }

  @Test
  public void should_format_xml_string_with_space_and_newline_prettily() {
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel>  <title>Java Tutorials and Examples 1</title>  \n\n<language>en-us</language>  </channel></rss>";
    assertThat(xmlPrettyFormat(xmlString)).isEqualTo(expected_formatted_xml);
  }

  @Test
  public void should_throw_error_when_xml_string_is_null() {
    thrown.expectWithMessageStartingWith(IllegalArgumentException.class, "Expecting XML String not to be null");
    xmlPrettyFormat(null);
  }

  @Test
  public void should_throw_error_when_xml_string_is_not_valid() {
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel><title>Java Tutorials and Examples 1</title><language>en-us</language></chnel></rss>";
    try {
      xmlPrettyFormat(xmlString);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(RuntimeException.class).hasMessageStartingWith("Unable to format XML string");
      assertThat(e).hasRootCauseInstanceOf(SAXParseException.class);
      assertThat(e.getCause()).hasMessageContaining(
          "The element type \"channel\" must be terminated by the matching end-tag \"</channel>\"");
    }
  }

}
