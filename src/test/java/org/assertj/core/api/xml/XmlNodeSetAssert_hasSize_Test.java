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
 * <li><code>{@link XmlNodeSetAssert#hasSize(Integer)}</code>
 * </ul>
 * 
 * @author Łukasz Strzelecki
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert_hasSize_Test extends AbstractXmlNodeSetAssertTest{

  @Override
  protected XmlNodeSetAssert invoke_successfully_method_under_test(XmlNodeSetAssert originalAssertion) {
    return originalAssertion.hasSize(1);
  }
  
  @Test
  public void should_pass_if_size_is_equal_to_expected() throws Exception {

    assertThat(xml).asXml().extractingXPath("//continent").hasSize(7);
    assertThat(xml).asXml().extractingXPath("//continent[@inhabited='true']").hasSize(6);
  }

  @Test
  public void should_fail_if_size_is_not_equal_to_expected() throws Exception {

    thrown.expect(AssertionError.class); 

    assertThat(xml).asXml().extractingXPath("//continent").hasSize(8);
  }

  @Test
  public void should_fail_if_size_is_not_equal_to_expected_empty_list() throws Exception {

    thrown.expectAssertionError("Expected size:<1> but was:<0> in:\n" 
        + "<[]>");

    assertThat(xml).asXml().extractingXPath("//continent[@name='Atlantis']").hasSize(1);
  }

}
