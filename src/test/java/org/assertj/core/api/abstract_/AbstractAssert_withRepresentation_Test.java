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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class AbstractAssert_withRepresentation_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_description_is_null() {
    thrown.expectNullPointerException("The representation to use should not be null.");
    assertThat(new Example()).withRepresentation(null);
  }

  @Test
  public void should_be_able_to_use_a_custom_representation_for_error_messages() {
    thrown.expectAssertionError("expected:<null> but was:<Example>");
    assertThat(new Example()).withRepresentation(new CustomRepresentation())
                             .isNull();
  }

  @Test
  public void should_be_able_to_override_an_existing_representation() {
    thrown.expectAssertionErrorWithMessageContaining("$foo$", "$bar$");
    assertThat("foo").withRepresentation(new CustomRepresentation())
                       .startsWith("bar");
  }

  private class Example {
  }

  private class CustomRepresentation extends StandardRepresentation {
    @Override
    public String toStringOf(Object o) {
      if (o instanceof Example) return "Example";
      return super.toStringOf(o);
    }

    @Override
    protected String toStringOf(String s) {
      return "$" + s + "$";
    }
  }
}
