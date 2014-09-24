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
 * <li><code>{@link XmlNodeSetAssert#isEqualTo(String)}</code>
 * </ul>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_isEqualTo_Test extends AbstractXmlNodeSetAssertTest{
  
  @Override
  protected XmlNodeSetAssert invoke_successfully_method_under_test(XmlNodeSetAssert originalAssertion) {
    return originalAssertion.isEqualTo(xml);
  }
  
  @Test
  public void should_fail_if_no_extracted_nodes() throws Exception {

    thrown.expectAssertionError("Expected to contain single node, but no nodes have been found!");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Atlantis']/text()").isEqualTo("");
  }

  @Test
  public void should_fail_if_more_than_one_extracted_node() throws Exception {
    
    thrown.expectAssertionError("Expected to contain single node, but multiple nodes have been found!");
    
    assertThat(xml).asXml().extractingXPath("//continent/area/text()").isEqualTo("");
  }
  
  @Test
  public void should_fail_if_element_is_not_equal() throws Exception {
    
    thrown.expectAssertionError("expected:<\n" +
    		"<area>10[]</area>\n" +
    		"> but was:<\n" +
    		"<area>10[180000]</area>\n");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/area").isEqualTo("<area>10</area>");
  }
 
  @Test
  public void should_pass_if_element_is_equal() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/area").isEqualTo("<area>10180000</area>");
  }
  
  public void should_fail_if_attribute_is_not_equal() throws Exception {
    
    thrown.expectAssertionError("expected:<@inhabited=\"[tru]e\"> but was:<@inhabited=\"[fals]e\">");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Antarctica']/@inhabited").isEqualTo("@inhabited='true'");
  }
  
  @Test
  public void should_pass_if_attribute_is_equal() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Antarctica']/@inhabited").isEqualTo("@inhabited='false'");
  }
  
  @Test
  public void should_fail_if_comment_is_not_equal() throws Exception {
    
    thrown.expectAssertionError("expected:<\n" +
    		"<!-- [Not that big] -->\n" +
    		"> but was:<\n" +
    		"<!-- [Largest continent] -->\n" +
    		">");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Asia']/comment()").isEqualTo("<!-- Not that big -->");
  }

  @Test
  public void should_pass_if_comment_is_equal() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Asia']/comment()").isEqualTo("<!-- Largest continent -->");
  }

  @Test
  public void should_fail_if_text_node_is_not_equal() throws Exception {
    
    thrown.expectAssertionError("expected:<\n" +
    		"10[]\n" +
    		"> but was:<\n" +
    		"10[180000]\n" +
    		">");
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/area/text()").isEqualTo("10");
  }
  
  @Test
  public void should_pass_if_text_node_is_equal() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent[@name='Europe']/area/text()").isEqualTo("10180000");
  }
  
}
