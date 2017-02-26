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
package org.example.custom;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.internal.Failures;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomAsserts_filter_stacktrace_Test {

  @Test
  public void should_filter_when_custom_assert_fails_with_message() {
    try {
      new CustomAssert("").fail();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementOf(CustomAssert.class));
    }
  }
  
  @Test
  public void should_filter_when_custom_assert_fails_with_overridden_message() {
    try {
      new CustomAssert("").overridingErrorMessage("overridden message").fail();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementOf(CustomAssert.class));
    }
  }
  
  @Test
  public void should_filter_when_custom_assert_throws_assertion_error() {
    try {
      new CustomAssert("").throwAnAssertionError();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementOf(CustomAssert.class));
    }
  }

  @Test
  public void should_filter_when_abstract_custom_assert_fails() {
    try {
      new CustomAssert("").failInAbstractAssert();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementOf(CustomAbstractAssert.class));
    }
  }

  @Test
  public void should_not_filter_when_global_remove_option_is_disabled() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
    try {
      new CustomAssert("").fail();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areAtLeastOne(elementOf(CustomAssert.class));
    }
  }
  
  @Before
  @After
  public void enableStackTraceFiltering() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(true);
  }

  private static Condition<StackTraceElement> elementOf(final Class<?> clazz) {
    return new Condition<StackTraceElement>("StackTraceElement of " + clazz) {
      @Override
      public boolean matches(StackTraceElement value) {
        return value.getClassName().equals(clazz.getName());
      }
    };
  }

  private static class CustomAssert extends CustomAbstractAssert<CustomAssert> {

    public CustomAssert(String actual) {
      super(actual, CustomAssert.class);
    }

    public CustomAssert fail() {
      failWithMessage("failing assert");
      return this;
    }
    
    public CustomAssert throwAnAssertionError() {
      throwAssertionError(new BasicErrorMessageFactory("failing assert"));
      return this;
    }
  }

  private static class CustomAbstractAssert<S extends CustomAbstractAssert<S>> extends AbstractObjectAssert<S, String> {

    protected CustomAbstractAssert(String actual, Class<?> selfType) {
      super(actual, selfType);
    }

    public S failInAbstractAssert() {
      failWithMessage("failing assert");
      return myself;
    }
  }
}
