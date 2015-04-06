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

import org.assertj.core.internal.Uris;
import org.assertj.core.util.VisibleForTesting;

import java.net.URI;

/**
 * Base class for all implementations of assertions for {@link URI}s.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @see java.net.URI
 *
 * @author Alexander Bischof
 */
public abstract class AbstractUriAssert<S extends AbstractUriAssert<S>> extends AbstractAssert<S, URI> {

  @VisibleForTesting
  protected Uris uris = Uris.instance();

  protected AbstractUriAssert(final URI actual, final Class<?> selfType) {
	super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code URI} has the expected scheme.
   * <p>
   * This assertion will succeed:
   *
   * <pre><code class='java'>
   * assertThat(new URI("http://helloworld.org")).hasScheme("http");
   * assertThat(new URI("ftp://helloworld.org")).hasScheme("ftp");
   * assertThat(new URI("helloworld.org")).hasScheme(null);
   * </code></pre>
   *
   * Whereas this assertion will fail:
   *
   * <pre><code class='java'>
   * assertThat(new URI("http://helloworld.org")).hasScheme("ftp");
   * assertThat(new URI("http://helloworld.org")).hasScheme(null);
   * assertThat((URI)null).hasScheme(null);
   * </code></pre>
   *
   * @param expected the expected scheme of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual scheme is not equal to the expected scheme.
   * @throws java.net.URISyntaxException if actual can not be parsed as a URI reference.
   */
  public S hasScheme(String expected) {
	uris.assertHasScheme(info, actual, expected);
	return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected path.
   * <p>
   * This assertion will succeed:
   *
   * <pre><code class='java'>
   * assertThat(new URI("http://helloworld.org/pages")).hasPath("/pages/")
   * </code></pre>
   *
   * Whereas this assertion will fail:
   *
   * <pre><code class='java'>
   * assertThat(new URI("http://helloworld.org/pickme")).hasPath("/pages/")
   * </code></pre>
   *
   * @param expected the expected path of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual path is not equal to the expected path.
   */
  public S hasPath(String expected) {
      uris.assertHasPath(info, actual, expected);
      return myself;
  }
}
