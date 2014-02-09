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

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.xml.XmlStringPrettyFormatter;
import org.junit.Rule;

/**
 * Test data for {@link XmlNodeSetAssert} tests.  
 * 
 * @author Michał Piotrkowski
 */
public class AbstractXmlNodeSetAssertTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  protected String europe = xml(
      "<continent name='Europe' inhabited='true'>" +
          "<!-- Actually, it is not considered as real a continent by some people.  -->" + 
          "<area>10180000</area>" +
      "</continent>");
  protected String asia = xml(
      "<continent name='Asia' inhabited='true'>" +
          "<!-- Largest continent -->" + 
          "<area>43820000</area>" +
      "</continent>");
  protected String northAmerica = xml(
        "<continent name='North America' inhabited='true'>" +
            "<area>24490000</area>" +
        "</continent>");
  protected String southAmerica = xml(
        "<continent name='South america' inhabited='true'>" +
            "<area>17840000</area>" +
        "</continent>");
  protected String australia = xml(
        "<continent name='Australia' inhabited='true'>" +
            "<area>9008500</area>" +
        "</continent>");
  protected String africa = xml( 
      "<continent name='Africa' inhabited='true'>" +
          "<area>30370000</area>" +
      "</continent>");
  protected String antarctica = xml(
        "<continent name='Antarctica' inhabited='false'>" +
            "<!-- Coldest continent -->" + 
            "<area>13720000</area>" +
        "</continent>");
  protected String atlantis = xml(
        "<continent name=\"Atlantis\">" +
          "<area>???</area>" +
        "</continent>");
  protected String xml = xml(
        "<continents>" +
          europe + 
          asia + 
          northAmerica  + 
          southAmerica  + 
          australia  +
          africa +
          antarctica + 
        "</continents>");

  private String xml(String xml) {
    return XmlStringPrettyFormatter.xmlPrettyFormat(xml);
  }

}