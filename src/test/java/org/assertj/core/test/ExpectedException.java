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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.test;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.introspection.IntrospectionError;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
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

  public void expectAssertionErrorWithMessageContaining(String... parts) {
    expectWithMessageContaining(AssertionError.class, parts);
  }

  public void expectNullPointerException(String message) {
    expect(NullPointerException.class, message);
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

  public void expect(Throwable error) {
    expect(error.getClass());
    expectMessage(error.getMessage());
  }

  public void expect(Class<? extends Throwable> type) {
    delegate.expect(type);
  }

  public void expectMessage(String message) {
    delegate.expectMessage(String.format(message));
  }

  private void expectMessageContaining(String... parts) {
    List<Matcher<? super String>> matchers = new ArrayList<>();
    for (String part : parts) {
      matchers.add(StringContains.containsString(part));
    }
    delegate.expectMessage(AllOf.allOf(matchers));
  }

}
