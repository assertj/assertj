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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.description.Description;

/**
 * {@link ThrowableAssertAlternative} subclass used in soft assertions.
 * <p> 
 * Assertion methods for {@link java.lang.Throwable} similar to {@link ThrowableAssert} but with assertions methods named
 * differently to make testing code fluent (ex : <code>withMessage</code> instead of <code>hasMessage</code>).
 * <pre><code class='java'> SoftAssertions softly = new SoftAssertions();
 * 
 * softly.assertThatExceptionOfType(IOException.class)
 *           .isThrownBy(() -&gt; { throw new IOException("boom! tcha!"); });
 *           .withMessage("boom! %s", "tcha!"); </code></pre>
 *           
 * This class is linked with the {@link ThrowableTypeAssert} and allow to check that an exception type is thrown by a lambda.
 * 
 * @since 3.23.0
 */
public class SoftThrowableAssertAlternative<ACTUAL extends Throwable> extends ThrowableAssertAlternative<ACTUAL> {

  private final ThrowableAssert<ACTUAL> proxiedThrowableAssert;

  @SuppressWarnings("unchecked")
  public SoftThrowableAssertAlternative(final ACTUAL actual, SoftAssertionsProvider softAssertionsProvider) {
    super(actual);
    proxiedThrowableAssert = softAssertionsProvider.proxy(ThrowableAssert.class, Throwable.class, actual);
  }

  @Override
  public SoftThrowableAssertAlternative<ACTUAL> as(Description description) {
    super.as(description);
    return this;
  }

  @Override
  protected ThrowableAssert<ACTUAL> getDelegate() {
    return proxiedThrowableAssert;
  }
}
