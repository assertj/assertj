package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import java.security.Certificate;

import org.junit.jupiter.api.Test;

public class InstanceOfAssertFactoriesTest {

  @Test
  public void string_factory_should_allow_string_assertions() {
    assertThat((Object) "string").instanceOf(STRING).startsWith("str");
  }

  @Test
  public void integer_factory_should_allow_integer_assertions() {
    assertThat((Object) 0).instanceOf(INTEGER).isZero();
  }

  @Test
  @SuppressWarnings("deprecation")
  public void class_factory_should_allow_class_assertions() {
    assertThat((Object) Certificate.class).instanceOf(CLASS).hasAnnotations(Deprecated.class);
  }

}
