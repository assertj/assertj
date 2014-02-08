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

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.xml.XmlStringPrettyFormatter;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for:
 * <ul>
 * <li><code>{@link XmlNodeSetAssert#contains(String)}</code>
 * </ul>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_contains_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private String europe = xml(
    "<continent name='Europe' inhabited='true'>" +
        "<!-- AssertJ was created here! -->" + 
        "<area>10180000</area>" +
    "</continent>");
  
  private String asia = xml(
    "<continent name='Asia' inhabited='true'>" +
        "<!-- Largest continent -->" + 
        "<area>43820000</area>" +
    "</continent>");
  
  private String northAmerica = xml(
      "<continent name='North America' inhabited='true'>" +
          "<area>24490000</area>" +
      "</continent>");
  
  private String southAmerica = xml(
      "<continent name='South america' inhabited='true'>" +
          "<area>17840000</area>" +
      "</continent>");
  
  private String australia = xml(
      "<continent name='Australia' inhabited='true'>" +
          "<area>9008500</area>" +
      "</continent>");

  private String africa = xml( 
    "<continent name='Africa' inhabited='true'>" +
        "<area>30370000</area>" +
    "</continent>");
  
  private String antarctica = xml(
      "<continent name='Antarctica' inhabited='false'>" +
          "<area>13720000</area>" +
      "</continent>");
  
  private String atlantis = xml(
      "<continent name=\"Atlantis\">" +
        "<area>???</area>" +
      "</continent>");
  
  private String xml = xml(
      "<continents>" +
        europe + 
        asia + 
        northAmerica  + 
        southAmerica  + 
        australia  +
        africa +
        antarctica + 
      "</continents>");

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
  
  // --
  
  private String xml(String xml){
    return XmlStringPrettyFormatter.xmlPrettyFormat(xml);
  }
  
}
