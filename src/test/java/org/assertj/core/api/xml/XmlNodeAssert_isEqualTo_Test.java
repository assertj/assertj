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

import static org.assertj.core.api.xml.XmlNodeAssertions.assertFor;
import static org.assertj.core.util.xml.XmlUtil.toStringOf;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.util.xml.XmlUtil;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Node;

/**
 * Tests for {@link XmlTextNodeAssert#isTextNode()}.  
 * 
 * @author Michał Piotrkowski
 */
@RunWith(Parameterized.class)
public class XmlNodeAssert_isEqualTo_Test extends AbstractXmlNodeAssertTest{

  private final Node node;

  public XmlNodeAssert_isEqualTo_Test(Node node) {
    this.node = node;
  }

  @Parameters
  public static List<Node[]> testedNodeTypes() {
    return Arrays.<Node[]>asList(
        node("<element/>"),
        node("@attribute='value'"),
        node("<!-- comment -->"),
        node("text node")
        );
  }

  @Override
  protected XmlNodeAssert create_original_xml_assertion() {
    return assertFor(node);
  }

  @Override
  protected XmlNodeAssert invoke_successfully_method_under_test(XmlNodeAssert originalAssertion) {
    return originalAssertion.isEqualTo(toStringOf(node));
  }
  
  public static Node[] node(String xml) {
    return new Node[]{ XmlUtil.parseNode(xml) };
  }
}

