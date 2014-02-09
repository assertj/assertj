/*
 * Created on Feb 08, 2014
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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Xmls;
import org.w3c.dom.Node;

/**
 * Generic implementation of {@link XmlNodeAssert} handling xml nodes. Has to be overriden by subclasses.  
 * 
 * @author Michał Piotrkowski
 */
abstract class AbstractXmlNodeAssert extends AbstractAssert<AbstractXmlNodeAssert, Node> implements XmlNodeAssert {

  protected Xmls xmls = Xmls.instance();
  
  protected AbstractXmlNodeAssert(Node actual, Class<?> selfType) {
    super(actual, selfType);
  }

  protected abstract String type();

  @Override
  public XmlNodeAssert isElement() {
    xmls.failNotElementBut(info, type());
    return null;
  }

  @Override
  public XmlNodeAssert isAttribute() {
    xmls.failNotAttributeBut(info, type());
    return null;
  }
  
  @Override
  public XmlNodeAssert isComment() {
    xmls.failNotCommentBut(info, type());
    return null;
  }

  @Override
  public XmlNodeAssert isTextNode() {
    xmls.failNotTextNodeBut(info, type());
    return null;
  }
  
  @Override
  public XmlNodeAssert isEqualTo(String expectedXml) {
    xmls.assertEqual(info, actual, expectedXml);
    return myself;
  }
}
