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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.test.ExpectedException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for:
 * <ul>
 * <li><code>{@link XmlNodeSetAssert#isEqualTo(String)}</code>
 * </ul>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_contains_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private String xml = "<continents>" +
                          "<continent name='Europe' inhabited='true'>" +
                            "<!-- AssertJ was created here! -->" + 
                            "<area>10180000</area>" +
                          "</continent>" + 
                          "<continent name='Asia' inhabited='true'>" +
                            "<!-- Largest continent -->" + 
                            "<area>43820000</area>" +
                          "</continent>" + 
                          "<continent name='North America' inhabited='true'>" +
                            "<area>24490000</area>" +
                          "</continent>" + 
                          "<continent name='South america' inhabited='true'>" +
                            "<area>17840000</area>" +
                          "</continent>" + 
                          "<continent name='Australia' inhabited='true'>" +
                            "<area>9008500</area>" +
                          "</continent>" + 
                          "<continent name='Africa' inhabited='true'>" +
                            "<area>30370000</area>" +
                          "</continent>" + 
                          "<continent name='Antarctica' inhabited='false'>" +
                            "<area>13720000</area>" +
                          "</continent>" + 
  		               "</continents>";

  @Ignore
  @Test
  public void should_fail_if_node_is_not_present() throws Exception {

    String searchedElement = 
        "<continent name='Atlantis'>" +
          "<area>???</area>" +
        "</continent>";
    
    // expect:
    thrown.expectAssertionError(String.format("Expecting: <%s> to contain: <%s> but could not find: <%s>", xml, searchedElement, searchedElement));
    
    // when:
    assertThat(xml).asXml().extractingXPath("//continent").contains(searchedElement);
  }
  
}
