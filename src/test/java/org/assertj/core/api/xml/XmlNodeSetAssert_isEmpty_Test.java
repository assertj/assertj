/*
 * Created on Feb 04, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.api.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for: 
 * <ul>
 *  <li><code>{@link XmlNodeSetAssert#isEmpty}</code>
 * </ul>
 * 
 * @author Lukasz Strzelecki
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_isEmpty_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void should_fail_if_result_not_empty() throws Exception {

    // expect:
    thrown.expectAssertionError("Expecting empty but was:<\"<node>value</node>\">");
    
    // when:
    assertThat("<node>value</node>").asXml().extractingXPath("/*").isEmpty();
  }

  @Test
  public void should_pass_if_result_is_empty() throws Exception {

    // when:
    assertThat("<node>value</node>").asXml().extractingXPath("/otherNode").isEmpty();
  }

}
