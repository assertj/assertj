package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.internal.Strings;
import org.junit.Test;

/**
 * Tests for {@link Assertions#assertThat(AssertProvider)}.
 * 
 * @author Tobias Liefke
 */
public class Assertions_assertThat_with_AssertProvider_Test {

  @Test
  public void should_allow_assert_provider_within_assertThat() {
    TestedObject object = new TestedObject("Test");
    assertThat(object).containsText("es");
  }

  private static class TestedObject implements AssertProvider<TestedObjectAssert> {
    private final String text;

    public TestedObject(String text) {
      this.text = text;
    }

    public TestedObjectAssert assertThat() {
      return new TestedObjectAssert(this);
    }
  }

  private static class TestedObjectAssert extends AbstractAssert<TestedObjectAssert, TestedObject> {
    private Strings strings = Strings.instance();
    
    public TestedObjectAssert(TestedObject testedObject) {
      super(testedObject, TestedObjectAssert.class);
    }

    public TestedObjectAssert containsText(String text) {
      strings.assertContains(info, actual.text, text);
      return this;
    }
  }

}
