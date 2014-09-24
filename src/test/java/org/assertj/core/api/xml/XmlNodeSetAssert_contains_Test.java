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
 * <li><code>{@link XmlNodeSetAssert#contains(String...)}</code>
 * </ul>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_contains_Test extends AbstractXmlNodeSetAssertTest {

  @Override
  protected XmlNodeSetAssert invoke_successfully_method_under_test(XmlNodeSetAssert originalAssertion) {
    return originalAssertion.contains(xml);
  }
  
  @Test
  public void should_fail_if_node_has_not_been_extracted() throws Exception {

    String missingContinent = atlantis;
    
    thrown.expect(AssertionError.class);
    
    assertThat(xml).asXml().extractingXPath("//continent").contains(missingContinent);
  }

  @Test
  public void should_fail_meaningfully_if_null_passed() throws Exception {

    thrown.expectNullPointerException("Expected node cannot be null!");
    
    assertThat(xml).asXml().extractingXPath("//continent").contains((String[])null);
  }
  
  @Test
  public void should_fail_meaningfully_if_null_passed_as_one_of_elements() throws Exception {
    
    thrown.expectNullPointerException("Expected node cannot be null!");
    
    assertThat(xml).asXml().extractingXPath("//continent").contains(australia, null);
  }
  
  @Test
  public void should_pass_if_element_has_been_extracted() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent").contains(australia);
  }
  
  @Test
  public void should_pass_if_all_of_nodes_have_been_extracted() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent").contains(australia, africa);
  }
  
  @Test
  public void should_fail_if_at_least_one_of_nodes_has_not_been_extracted() throws Exception {

    thrown.expect(AssertionError.class);
    
    assertThat(xml).asXml().extractingXPath("//continent").contains(australia, atlantis);
  }

}
