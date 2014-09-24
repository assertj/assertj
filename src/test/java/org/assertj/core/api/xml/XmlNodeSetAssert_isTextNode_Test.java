/*
 * Created on Feb 04, 2014
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
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.api.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for:
 * <ul>
 * <li><code>{@link XmlNodeSetAssert#isTextNode()}</code>
 * </ul>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_isTextNode_Test extends AbstractXmlNodeSetAssertTest{

  @Override
  protected XmlNodeSetAssert create_original_xml_assertion() {
    return super.create_original_xml_assertion().extractingXPath("//continent[@name='Europe']/area/text()");
  }
  
  @Override
  protected XmlNodeSetAssert invoke_successfully_method_under_test(XmlNodeSetAssert originalAssertion) {
    return originalAssertion.isTextNode();
  }
  
  @Test
  public void should_fail_if_no_extracted_nodes() throws Exception {

    thrown.expectAssertionError("Expected to contain single node, but no nodes have been found!");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Atlantis']/text()").isTextNode();
  }

  @Test
  public void should_fail_if_more_than_one_extracted_node() throws Exception {
    
    thrown.expectAssertionError("Expected to contain single node, but multiple nodes have been found!");
    
    assertThat(xml).asXml().extractingXPath("//continent/area/text()").isTextNode();
  }
  
  @Test
  public void should_pass_if_exacly_one_extracted_text_node() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/area/text()").isTextNode();
  }
  
  @Test
  public void should_fail_if_attribute_extracted() throws Exception {

    thrown.expectAssertionError("Expected to contain single Text node, but attribute have been found!");

    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/@name").isTextNode();
  }
  
  @Test
  public void should_fail_if_element_extracted() throws Exception {
    
    thrown.expectAssertionError("Expected to contain single Text node, but element have been found!");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']").isTextNode();
  }
  
  @Test
  public void should_fail_if_comment_extracted() throws Exception {
    
    thrown.expectAssertionError("Expected to contain single Text node, but comment have been found!");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/comment()").isTextNode();
  }
  
}
