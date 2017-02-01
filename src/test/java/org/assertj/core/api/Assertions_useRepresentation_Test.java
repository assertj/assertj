/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.Introspection_getProperty_Test.Example;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_useRepresentation_Test {

  @Rule
  public ExpectedException thrown = none();

  private CustomRepresentation customRepresentation = new CustomRepresentation();

  @After
  public void afterTest() {
    Assertions.useDefaultRepresentation();
  }

  @Test
  public void should_use_given_representation_in_assertion_error_messages() {
    Assertions.useRepresentation(customRepresentation);
    thrown.expectAssertionErrorWithMessageContaining("$foo$", "$bar$");
    assertThat("foo").startsWith("bar");
  }

  @Test
  public void should_use_default_representation_in_assertion_error_messages_after_calling_useDefaultRepresentation() {
    Assertions.useRepresentation(customRepresentation);
    Assertions.useDefaultRepresentation();
    try {
      assertThat("foo").startsWith("bar");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).doesNotContain("$foo$")
                                .doesNotContain("bar$");
      return;
    }
    fail("AssertionError expected");
  }

  private class CustomRepresentation extends StandardRepresentation {

    // override needed to hook specific formatting
    @Override
    public String toStringOf(Object o) {
      if (o instanceof Example) return "Example";
      // fallback to default formatting.
      return super.toStringOf(o);
    }

    // change String representation
    @Override
    protected String toStringOf(String s) {
      return "$" + s + "$";
    }
  }
}
