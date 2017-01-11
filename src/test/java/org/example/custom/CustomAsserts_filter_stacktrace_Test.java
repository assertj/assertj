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
      assertThat(e.getStackTrace()).areNot(elementFor(CustomAssert.class));
    }
  }
  
  @Test
  public void should_filter_when_custom_assert_fails_with_overridden_message() {
    try {
      new CustomAssert("").overridingErrorMessage("overridden message").fail();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementFor(CustomAssert.class));
    }
  }
  
  @Test
  public void should_filter_when_custom_assert_throws_assertion_error() {
    try {
      new CustomAssert("").throwAnAssertionError();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementFor(CustomAssert.class));
    }
  }

  @Test
  public void should_filter_when_abstract_custom_assert_fails() {
    try {
      new CustomAssert("").failInAbstractAssert();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areNot(elementFor(CustomAbstractAssert.class));
    }
  }

  @Test
  public void should_not_filter_when_global_remove_option_is_disabled() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
    try {
      new CustomAssert("").fail();
    } catch (AssertionError e) {
      assertThat(e.getStackTrace()).areAtLeastOne(elementFor(CustomAssert.class));
    }
  }
  
  @Before
  @After
  public void enableStackTraceFiltering() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(true);
  }

  private static Condition<StackTraceElement> elementFor(final Class<?> clazz) {
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
