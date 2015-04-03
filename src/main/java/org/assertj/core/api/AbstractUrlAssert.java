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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.Urls;
import org.assertj.core.util.VisibleForTesting;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * Base class for all implementations of assertions for {@link URL}s.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @see java.net.URL
 *
 * @author Alexander Bischof
 */
public abstract class AbstractUrlAssert<S extends AbstractUrlAssert<S>> extends AbstractAssert<S, URL> {

  @VisibleForTesting
  protected Urls urls = Urls.instance();

  protected AbstractUrlAssert(final URL actual, final Class<?> selfType) {
	super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code URL} has the expected scheme.
   * <p>
   * This assertion will succeed:
   *
   * <pre><code class='java'>
   * assertThat(new URL("http://helloworld.org").hasSchemeEqualsTo("http")
   * </code></pre>
   *
   * Whereas this assertion will fail:
   *
   * <pre><code class='java'>
   * assertThat(new URL("ftp://helloworld.org").hasSchemeEqualsTo("http")
   * </code></pre>
   *
   * @param expected the expected scheme of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual scheme is not equal to the expected scheme.
   */
  public S hasScheme(String expected) {
	urls.assertHasScheme(info, actual, expected);
	return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected path.
   * <p>
   * This assertion will succeed:
   *
   * <pre><code class='java'>
   * assertThat(new URL("http://helloworld.org/pages").hasPathEquals("/pages/")
   * </code></pre>
   *
   * Whereas this assertion will fail:
   *
   * <pre><code class='java'>
   * assertThat(new URL("http://helloworld.org/pickme").hasPathEquals("/pages/")
   * </code></pre>
   *
   * @param expected the expected path of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual path is not equal to the expected path.
   */
  public S hasPathEquals(String expected) {
      urls.assertHasScheme(info, actual, expected);
      return myself;
  }
}
