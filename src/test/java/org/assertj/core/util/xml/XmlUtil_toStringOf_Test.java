/*
 * Created on Feb 8, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2013 the original author or authors.
 */
package org.assertj.core.util.xml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.xml.XmlUtil.parseNode;
import static org.assertj.core.util.xml.XmlUtil.toStringOf;

import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Tests for <code>{@link XmlUtil#toStringOf(Node)}</code>.
 *
 * @author Michał Piotrkowski
 */
public class XmlUtil_toStringOf_Test {

  @Test
  public void should_print_simple_element() throws Exception {

    Node node = parseNode("<element>this is element</element>");
    assertThat(toStringOf(node)).isEqualTo("\n" +
    		"<element>this is element</element>\n");
  }
  
  @Test
  public void should_pretty_print_nested_element() throws Exception {
    
    Node node = parseNode("<complexElement><element>abc</element></complexElement>");
    assertThat(toStringOf(node)).isEqualTo("\n" +
        "<complexElement>\n" +
        "    <element>abc</element>\n" +
        "</complexElement>\n");
  }

  @Test
  public void should_print_element_with_attribute() throws Exception {

    Node node = parseNode("<element attribute=\"value\"/>");
    assertThat(toStringOf(node)).isEqualTo("\n" +
    		"<element attribute=\"value\"/>\n");
  }
  

  @Test
  public void should_print_attribute() throws Exception {
    
    Node node = parseNode("@attribute=\"value\"");
    assertThat(toStringOf(node)).isEqualTo("@attribute=\"value\"");
  }
    
  @Test
  public void should_print_comment() throws Exception {
    
    Node node = XmlUtil.parseNode("<!-- some comment -->");
    assertThat(toStringOf(node)).isEqualTo("\n" +
    		"<!-- some comment -->\n");
  }

  @Test
  public void should_print_text_node() throws Exception {
    
    Node node = XmlUtil.parseNode("text");
    assertThat(toStringOf(node)).isEqualTo("\n" +
    		"text\n");
  }

}

