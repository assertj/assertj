/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.test;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.error.AssertionErrorFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Allows in-test specification of expected exception types and messages.
 *
 * @author Alex Ruiz
 */
public class ExpectedException implements TestRule {
  private final org.junit.rules.ExpectedException delegate = org.junit.rules.ExpectedException.none();

  public static ExpectedException none() {
    return new ExpectedException();
  }

  private ExpectedException() {}

  @Override
  public Statement apply(Statement base, Description description) {
    return delegate.apply(base, description);
  }

  public void expectAssertionError(String message) {
    expect(AssertionError.class, message);
  }

  public void expectAssertionError(ErrorMessageFactory errorMessageFactory) {
    delegate.expect(new ThrowableMatcher<>(AssertionError.class, errorMessageFactory.create()));
  }

  public void expectAssertionError(AssertionErrorFactory assertionErrorFactory) {
    AssertionError assertionError = assertionErrorFactory.newAssertionError(null, CONFIGURATION_PROVIDER.representation());
    delegate.expect(new ThrowableMatcher<>(AssertionError.class, assertionError.getMessage()));
  }

  public void expectAssertionErrorWithMessageContaining(String... parts) {
    expectWithMessageContaining(AssertionError.class, parts);
  }

  public void expectNullPointerException(String message) {
    expect(NullPointerException.class, message);
  }

  public void expectIllegalArgumentException(String message) {
    expect(IllegalArgumentException.class, message);
  }

  private void expect(Class<? extends Throwable> type, String message) {
    expect(type);
    expectMessage(message);
  }

  private void expectWithMessageContaining(Class<? extends Throwable> type, String... parts) {
    expect(type);
    expectMessageContaining(parts);
  }

  public void expect(Class<? extends Throwable> type) {
    delegate.expect(type);
  }

  public void expectMessage(String message) {
    delegate.expectMessage(IsEqual.equalTo(format(message)));
  }

  private void expectMessageContaining(String... parts) {
    List<Matcher<? super String>> matchers = Arrays.stream(parts)
                                                   .map(part -> StringContains.containsString(format(part)))
                                                   .collect(toList());
    delegate.expectMessage(AllOf.allOf(matchers));
  }

  private static class ThrowableMatcher<T extends Throwable> extends TypeSafeMatcher<T> {

    private final Class<?> expectedType;
    private final String expectedMessage;

    public ThrowableMatcher(Class<?> expectedType, String expectedMessage) {
      this.expectedType = expectedType;
      this.expectedMessage = expectedMessage;
    }

    @Override
    public void describeTo(org.hamcrest.Description description) {
      description.appendText(buildStringRepresentation(expectedType, expectedMessage));
    }

    @Override
    protected boolean matchesSafely(T actual) {
      return expectedType.isInstance(actual) && expectedMessage.equals(actual.getMessage());
    }

    private String buildStringRepresentation(Class<?> clazz, String message) {
      return format("<%s: %s>", clazz.getName(), message);
    }
  }

}
