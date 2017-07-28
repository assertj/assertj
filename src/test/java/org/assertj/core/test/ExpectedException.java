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
package org.assertj.core.test;

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.error.AssertionErrorFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.util.introspection.IntrospectionError;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
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

  public void expectAssertionError() {
    expect(AssertionError.class);
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

  public void expectNullPointerException() {
    expect(NullPointerException.class);
  }

  public void expectNullPointerException(String message) {
    expect(NullPointerException.class, message);
  }

  public void expectIllegalArgumentException() {
    expect(IllegalArgumentException.class);
  }

  public void expectIllegalArgumentException(String message) {
    expect(IllegalArgumentException.class, message);
  }

  public void expectIndexOutOfBoundsException(String message) {
    expect(IndexOutOfBoundsException.class, message);
  }

  public void expectUnsupportedOperationException(String message) {
    expect(UnsupportedOperationException.class, message);
  }

  public void expectIntrospectionError() {
    expect(IntrospectionError.class);
  }

  public void expectIntrospectionErrorWithMessageContaining(String... parts) {
    expectWithMessageContaining(IntrospectionError.class, parts);
  }

  public void expect(Class<? extends Throwable> type, String message) {
    expect(type);
    expectMessage(message);
  }

  public void expectWithMessageContaining(Class<? extends Throwable> type, String... parts) {
    expect(type);
    expectMessageContaining(parts);
  }

  public void expectWithMessageStartingWith(Class<? extends Throwable> type, String start) {
    expect(type);
    expectMessageStartingWith(start);
  }

  public void expectWithMessageEndingWith(Class<? extends Throwable> type, String end) {
    expect(type);
    expectMessageEndingWith(end);
  }

  public void expect(Class<? extends Throwable> type) {
    delegate.expect(type);
  }

  public void expectWithCause(Class<? extends Throwable> type, Throwable cause) {
    expect(type);
    expectCause(cause);
  }

  public void expectWithCause(Class<? extends Throwable> type, String message, Throwable cause) {
    expect(type);
    expectMessage(message);
    expectCause(cause);
  }

  public void expectMessage(String message) {
    delegate.expectMessage(IsEqual.equalTo(format(message)));
  }

  private void expectMessageContaining(String... parts) {
    List<Matcher<? super String>> matchers = new ArrayList<>();
    for (String part : parts) {
      matchers.add(StringContains.containsString(format(part)));
    }
    delegate.expectMessage(AllOf.allOf(matchers));
  }

  private void expectMessageStartingWith(String start) {
    delegate.expectMessage(StringStartsWith.startsWith(format(start)));
  }

  private void expectMessageEndingWith(String end) {
    delegate.expectMessage(StringEndsWith.endsWith(format(end)));
  }

  private void expectCause(Throwable cause) {
    delegate.expectCause(IsSame.sameInstance(cause));
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
