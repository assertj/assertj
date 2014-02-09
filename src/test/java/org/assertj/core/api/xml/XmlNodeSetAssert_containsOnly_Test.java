/*
 * Created on Feb 09, 2014
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
 *  <li><code>{@link XmlNodeSetAssert#containsOnly(String...)}</code>
 * </ul>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_containsOnly_Test extends AbstractXmlNodeSetAssertTest {

  @Override
  protected XmlNodeSetAssert invoke_successfully_method_under_test(XmlNodeSetAssert originalAssertion) {
    return originalAssertion.containsOnly(xml);
  }
  
  @Test
  public void should_fail_meaningfully_if_null_passed() throws Exception {

    thrown.expectNullPointerException("Expected node cannot be null!");
    
    assertThat(xml).asXml().extractingXPath("//continent").containsOnly((String[])null);
  }
  
  @Test
  public void should_fail_meaningfully_if_null_passed_as_one_of_elements() throws Exception {
    
    thrown.expectNullPointerException("Expected node cannot be null!");
    
    assertThat(xml).asXml().extractingXPath("//continent").containsOnly(australia, null);
  }
  
  @Test
  public void should_pass_if_all_expected_has_been_extracted() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent").containsOnly(
        europe, 
        asia,
        northAmerica, 
        southAmerica, 
        australia,
        africa,
        antarctica);
  }
  
  @Test
  public void should_pass_if_all_expected_has_been_extracted_but_in_different_order() throws Exception {
    
    assertThat(xml).asXml().extractingXPath("//continent").containsOnly(
        antarctica,
        africa,
        australia,
        southAmerica, 
        northAmerica, 
        asia,
        europe);
  }
  
  @Test
  public void should_fail_if_at_least_one_additional_node_been_extracted() throws Exception {

    thrown.expect(AssertionError.class);
    
    assertThat(xml).asXml().extractingXPath("//continent").containsOnly(
        europe, 
        asia,
        northAmerica, 
        southAmerica, 
        australia,
        africa);
  }
  
  @Test
  public void should_fail_if_additional_nodes_has_been_expected() throws Exception {
    
    thrown.expect(AssertionError.class);
    
    assertThat(xml).asXml().extractingXPath("//continent").containsOnly(
        europe, 
        asia,
        northAmerica, 
        southAmerica, 
        australia,
        africa,
        antarctica,
        atlantis);
  }
  
}
