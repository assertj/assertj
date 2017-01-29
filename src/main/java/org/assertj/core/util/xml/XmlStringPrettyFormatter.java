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

import static org.assertj.core.util.Preconditions.checkArgument;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

/**
 * Format an XML String with indent = 2 space.
 * <p>
 * Very much inspired by http://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java and
 * http://pastebin.com/XL7932aC
 * </p>
 */
public class XmlStringPrettyFormatter {

  private static final String FORMAT_ERROR = "Unable to format XML string";

  public static String xmlPrettyFormat(String xmlStringToFormat) {
    checkArgument(xmlStringToFormat != null, "Expecting XML String not to be null");
    // convert String to an XML Document and then back to String but prettily formatted.
    return prettyFormat(toXmlDocument(xmlStringToFormat), xmlStringToFormat.startsWith("<?xml"));
  }

  private static String prettyFormat(Document document, boolean keepXmlDeclaration) {

    try {
      DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      DOMImplementationLS domImplementation = (DOMImplementationLS) registry.getDOMImplementation("LS");
      Writer stringWriter = new StringWriter();
      LSOutput formattedOutput = domImplementation.createLSOutput();
      formattedOutput.setCharacterStream(stringWriter);
      LSSerializer domSerializer = domImplementation.createLSSerializer();
      domSerializer.getDomConfig().setParameter("format-pretty-print", true);
      // Set this to true if the declaration is needed to be in the output.
      domSerializer.getDomConfig().setParameter("xml-declaration", keepXmlDeclaration);
      domSerializer.write(document, formattedOutput);
      return stringWriter.toString();
    } catch (Exception e) {
      throw new RuntimeException(FORMAT_ERROR, e);
    }
  }

  private static Document toXmlDocument(String xmlString) {
    try {
      InputSource xmlInputSource = new InputSource(new StringReader(xmlString));
      DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      return xmlDocumentBuilder.parse(xmlInputSource);
    } catch (Exception e) {
      throw new RuntimeException(FORMAT_ERROR, e);
    }
  }

  private XmlStringPrettyFormatter() {
    // utility class
  }
}
