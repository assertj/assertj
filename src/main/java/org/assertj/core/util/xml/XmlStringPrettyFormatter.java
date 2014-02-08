package org.assertj.core.util.xml;

import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * Format an XML String with indent = 2 space.
 * <p>
 * Very much inspired by http://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java and
 * http://pastebin.com/XL7932aC
 * </p>
 */
public class XmlStringPrettyFormatter {

  private static final String FORMAT_ERROR = "Unable to format XML string";
  private static final String PARSE_ERROR = "Unable to parse XML";

  public static String xmlPrettyFormat(String xmlStringToFormat) {
    if (xmlStringToFormat == null)
      throw new IllegalArgumentException("Expecting XML String not to be null");
    // convert String to an XML Document and then back to String but prettily formatted.
    return prettyFormat(toXmlDocument(xmlStringToFormat), xmlStringToFormat.startsWith("<?xml"));
  }

  private static String prettyFormat(Node node, boolean keepXmlDeclaration) {

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
      domSerializer.write(node, formattedOutput);
      return stringWriter.toString();
    } catch (Exception e) {
      throw new RuntimeException(FORMAT_ERROR, e);
    }
  }
  
  public static String prettyFormat(Node node){
      return prettyFormat(node, false);
  }

  public static Node toXmlDocument(String xmlString) {
    try {
      return XmlUtil.doParse(xmlString);
    } catch (Exception e) {
      throw new RuntimeException(PARSE_ERROR, e);
    }
  }

  private XmlStringPrettyFormatter() {
    // utility class
  }
}
