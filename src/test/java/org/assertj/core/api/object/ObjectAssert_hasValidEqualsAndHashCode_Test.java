package org.assertj.core.api.object;

import static java.util.Arrays.asList;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;

public class ObjectAssert_hasValidEqualsAndHashCode_Test extends ObjectAssertBaseTest {

  private Jedi other = new Jedi("Yoda", "Blue");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasValidEqualsAndHashCode(() -> asList(new Jedi("Yoda", "Green")), () -> asList(other));
  }

  @Override
  protected void verify_internal_effects() {
  }
}