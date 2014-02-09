package org.assertj.core.api.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public abstract class AbstractXmlNodeAssertTest {

  @Test
  public void should_support_fluent_chaining() throws Exception {

    XmlNodeAssert originalAssertion = create_original_xml_assertion();
    
    XmlNodeAssert assertionToChain = invoke_successfully_method_under_test(originalAssertion);
    
    verify_chained_assertion(originalAssertion, assertionToChain);
    
  }

  protected abstract XmlNodeAssert create_original_xml_assertion();
  
  protected void verify_chained_assertion(XmlNodeAssert originalAssertion, XmlNodeAssert assertionToChain) {
    assertThat(originalAssertion).isSameAs(assertionToChain);
  }

  protected abstract XmlNodeAssert invoke_successfully_method_under_test(XmlNodeAssert originalAssertion);
  
}
