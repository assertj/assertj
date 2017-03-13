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
package org.assertj.core.matcher;

import org.assertj.core.util.Throwables;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Generic Hamcrest {@link Matcher} that reuses AssertJ assertions.
 *
 * Overriding classes should only override {@link AssertionMatcher#assertion(Object)}
 * method. {@link Matcher#matches(Object)} and {@link Matcher#describeTo(Description)}
 * are provided. If matcher fails it will provide description that contains
 * stacktrace of a failed assertion.
 *
 * <p>
 * Example with Mockito use case:
 * <pre><code class='java'> verify(customerRepository).save(argThat(new AssertionMatcher&lt;Customer&gt;() {
 *   &#64;Override
 *   public void assertion(Customer actual) throws AssertionError {
 *     assertThat(actual)
 *       .hasName(&quot;John&quot;)
 *       .hasAge(30);
 *   }
 * }));</code></pre>
 * </p>
 *
 * @author Tomasz Kalkosiński
 */
public abstract class AssertionMatcher<T> extends BaseMatcher<T> {
  private AssertionError lastError;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean matches(Object argument) {
    T actual = (T) argument;
    try {
      assertion(actual);
      return true;
    } catch (AssertionError e) {
      lastError = e;
      return false;
    }
  }

  /**
   * Use assertions with <val>actual</val>.
   *
   * If any assertion fails matcher will also fail.
   *
   * @param actual assertion object
   * @throws AssertionError if the assertion object fails assertion
   */
  public abstract void assertion(T actual) throws AssertionError;

  /**
   * {@inheritDoc}
   */
  @Override
  public void describeTo(Description description) {
    if (lastError != null) {
      description.appendText("AssertionError with message: ");
      description.appendText(lastError.getMessage());
      description.appendText(String.format("%n%nStacktrace was: "));
      description.appendText(Throwables.getStackTrace(lastError));
    }
  }
}