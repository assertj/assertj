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

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for:
 * <ul>
 * <li><code>{@link XmlNodeSetAssert#extractingXPath(String)}</code>
 * </ul>
 * 
 * @author Łukasz Strzelecki
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_extractingXPath_Test extends AbstractXmlNodeSetAssertTest{
  
  @Override
  protected XmlNodeSetAssert invoke_successfully_method_under_test(XmlNodeSetAssert originalAssertion) {
    return originalAssertion.extractingXPath("/");
  }
  
  @Override
  protected void verify_chained_assertion(XmlNodeSetAssert originalAssertion, XmlNodeSetAssert assertionToChain) {

    // in this case we want to change 'actual' value of assertion,
    // so we have to return new assertion with other actual
    
    assertThat(assertionToChain).isNotNull();
    assertThat(originalAssertion).isNotSameAs(assertionToChain);
  }
  
  @Test
  public void should_extract_zero_elements() throws Exception {

    assertThat(xml).asXml().extractingXPath("//atlantis").hasSize(0);
  }

  @Test
  public void should_extract_some_elements() throws Exception {

    assertThat(xml).asXml().extractingXPath("//continent").hasSize(7);
    assertThat(xml).asXml().extractingXPath("//continent[@inhabited='true']").hasSize(6);
    assertThat(xml).asXml().extractingXPath("//continent[@inhabited='false']").hasSize(1);
  }

  @Test
  public void should_fail_meaningfully_if_invalid_xpath() throws Exception {
    
    thrown.expectIllegalArgumentException("Invalid xpath:<\"invalidXpath!\">");

    assertThat(xml).asXml().extractingXPath("invalidXpath!");
  }
  
  @Test
  public void should_fail_meaningfully_if_xpath_is_null() throws Exception {
    
    thrown.expectNullPointerException("XPath expression cannot be empty!");

    assertThat(xml).asXml().extractingXPath(null);
  }
  
  @Test
  public void should_fail_meaningfully_if_xpath_is_empty() throws Exception {
    
    thrown.expectIllegalArgumentException("XPath expression cannot be empty!");

    assertThat(xml).asXml().extractingXPath("");
  }
  
  @Test
  public void should_be_immutable() throws Exception {
    
    XmlNodeSetAssert xmlAssert = assertThat(xml).asXml();

    XmlNodeSetAssert expression1 = xmlAssert.extractingXPath("//continent[@inhabited='true']");
    XmlNodeSetAssert expression2 = xmlAssert.extractingXPath("//continent[@inhabited='false']");
    
    expression1.hasSize(6);
    expression2.hasSize(1);
  }
  
  @Ignore
  @Test
  public void should_be_chainable() throws Exception {
    
    XmlNodeSetAssert xmlAssert = assertThat(xml).asXml();
    
    xmlAssert.extractingXPath("//continent[@name='Europe']").extractingXPath("//area").hasSize(1);
    
  }

}
