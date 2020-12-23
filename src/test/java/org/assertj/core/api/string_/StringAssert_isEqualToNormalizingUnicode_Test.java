package org.assertj.core.api.string_;

import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

public class StringAssert_isEqualToNormalizingUnicode_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    return assertions.isEqualToNormalizingUnicode("\u0041\u0308");
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), "\u00C4");

  }

  @Test
  public void should_pass_equals_normalized_String_when_compared_with_normalized_String(){
    // GIVEN
    String string = "\u00C4";
    // THEN
    assertThat(string).isEqualToNormalizingUnicode("\u0041\u0308");
  }

  @Test
  public void should_fail_if_denormalized_String_is_compared_with_normalized_String(){
    // GIVEN
    String string = "\u0041\u0308";
    // THEN
    expectAssertionError(() -> assertThat(string).isEqualToNormalizingUnicode("\u0041\u0308"));
  }


}
