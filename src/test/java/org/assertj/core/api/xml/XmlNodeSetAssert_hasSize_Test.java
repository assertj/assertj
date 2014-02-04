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
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for:
 * <ul>
 * <li><code>{@link XmlNodeSetAssert#hasSize(Integer)}</code>
 * </ul>
 * 
 * @author Lukasz Strzelecki
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_hasSize_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_pass_if_size_is_equal_to_expected() throws Exception {

    // when:
    assertThat("<numbers><number>1</number></numbers>").asXml().extractingXPath("//number").hasSize(1);
    assertThat("<numbers><number>1</number><number>2</number><number>3</number></numbers>").asXml().extractingXPath("//number").hasSize(3);
  }

  @Test
  public void should_fail_if_size_is_not_equal_to_expected() throws Exception {

    // expect:
    thrown.expectAssertionError("Expected size:<1> but was:<2> in:\n" 
        + "<\"[<number>1</number>, <number>2</number>]\">");

    // when:
    assertThat("<numbers><number>1</number><number>2</number></numbers>").asXml().extractingXPath("//number").hasSize(1);
  }

  @Test
  public void should_fail_if_size_is_not_equal_to_expected_empty_list() throws Exception {

    // expect:
    thrown.expectAssertionError("Expected size:<1> but was:<0> in:\n" 
        + "<\"[]\">");

    // when:
    assertThat("<numbers><number>1</number></numbers>").asXml().extractingXPath("//matchingNothing").hasSize(1);
  }

}
