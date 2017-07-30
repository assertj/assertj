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
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.test.ExpectedException.none;

import java.util.NoSuchElementException;
import java.util.function.Function;

import org.assertj.core.api.ThrowableTypeAssert;
import org.assertj.core.description.TextDescription;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ThrowableTypeAssert_description_Test {

  @Rule
  public ExpectedException thrown = none();

  private Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder;

  public ThrowableTypeAssert_description_Test(Function<ThrowableTypeAssert<?>, ThrowableTypeAssert<?>> descriptionAdder) {
    this.descriptionAdder = descriptionAdder;
  }

  @Parameters
  public static Object[][] getParameters() {
    return new Function[][] {
        { t -> ((ThrowableTypeAssert<?>) t).as("test description") },
        { t -> ((ThrowableTypeAssert<?>) t).describedAs("test description") },
        { t -> ((ThrowableTypeAssert<?>) t).as(new TextDescription("%s description", "test")) },
        { t -> ((ThrowableTypeAssert<?>) t).describedAs(new TextDescription("%s description", "test")) }
    };
  }

  @Test
  public void should_contain_provided_description_if_nothing_is_thrown_by_lambda() {
    thrown.expectAssertionError("[test description] %nExpecting code to raise a throwable.");
    descriptionAdder.apply(assertThatExceptionOfType(NoSuchElementException.class)).isThrownBy(() -> {});
  }

  @Test
  public void should_contain_provided_description_when_exception_type_is_wrong() {
    thrown.expectAssertionErrorWithMessageContaining("[test description] %n" +
                                                     "Expecting:%n" +
                                                     "  <java.lang.IllegalArgumentException>%n" +
                                                     "to be an instance of:%n" +
                                                     "  <java.util.NoSuchElementException>");

    descriptionAdder.apply(assertThatExceptionOfType(NoSuchElementException.class)).isThrownBy(() -> {
      throw new IllegalArgumentException();
    });
  }

  @Test
  public void should_contain_provided_description_when_exception_message_is_wrong() {
    thrown.expectAssertionError("[test description] %n" +
                                "Expecting message:%n" +
                                " <\"other cause\">%n" +
                                "but was:%n" +
                                " <\"some cause\">");

    descriptionAdder.apply(assertThatIllegalArgumentException()).isThrownBy(() -> {
      throw new IllegalArgumentException("some cause");
    }).withMessage("other cause");
  }
}
