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

import java.net.URI;

import org.assertj.core.internal.Uris;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link URI}s.
 *
 * @param <S> the "self" type of this assertion class.
 * @see java.net.URI
 */
public abstract class AbstractUriAssert<S extends AbstractUriAssert<S>> extends AbstractAssert<S, URI> {

  @VisibleForTesting
  protected Uris uris = Uris.instance();

  protected AbstractUriAssert(final URI actual, final Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code URI} has the expected path.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://helloworld.org/pages")).hasPath("/pages");
   * assertThat(new URI("http://www.helloworld.org")).hasPath("");
   * 
   * // this assertion fails:
   * assertThat(new URI("http://helloworld.org/pickme")).hasPath("/pages");</code></pre>
   *
   * @param expected the expected path of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual URI path is not equal to the expected path.
   */
  public S hasPath(String expected) {
    uris.assertHasPath(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has no path.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("mailto:java-net@java.sun.com")).hasNoPath();
   * 
   * // this assertions fail:
   * assertThat(new URI("http://helloworld.org")).hasNoPath(); // empty path
   * assertThat(new URI("http://helloworld.org/france")).hasNoPath();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a path.
   */
  public S hasNoPath() {
    uris.assertHasPath(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected port.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://helloworld.org:8080")).hasPort(8080);
   * 
   * // These assertions fail:
   * assertThat(new URI("http://helloworld.org:8080")).hasPort(9876);
   * assertThat(new URI("http://helloworld.org")).hasPort(8080);
   * assertThat(new URI("helloworld.org:8080")).hasPort(8080);</code></pre>
   *
   * @param expected the expected port of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual port is not equal to the expected port.
   */
  public S hasPort(int expected) {
    uris.assertHasPort(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has no port.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://helloworld.org")).hasNoPort();
   * 
   * // These assertion fails:
   * assertThat(new URI("http://helloworld.org:8080")).hasNoPort();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a port.
   */
  public S hasNoPort() {
    uris.assertHasPort(info, actual, -1);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected host.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://helloworld.org")).hasAuthority("helloworld.org");
   * assertThat(new URI("http://helloworld.org/pages")).hasHost("helloworld.org");
   * assertThat(new URI("http://helloworld.org:8080")).hasHost("helloworld.org");
   * 
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org")).hasHost("helloworld.org");
   * assertThat(new URI("http://www.helloworld.org:8080")).hasHost("helloworld.org");</code></pre>
   *
   * @param expected the expected host of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual host is not equal to the expected host.
   */
  public S hasHost(String expected) {
    uris.assertHasHost(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected authority.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://helloworld.org:8080")).hasAuthority("helloworld.org:8080");
   * assertThat(new URI("http://www.helloworld.org:8080/news")).hasAuthority("www.helloworld.org:8080");
   * 
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org:8080")).hasAuthority("www.helloworld.org");
   * assertThat(new URI("http://www.helloworld.org")).hasAuthority("www.helloworld.org:8080");</code></pre>
   *
   * @param expected the expected authority of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual authority is not equal to the expected authority.
   */
  public S hasAuthority(String expected) {
    uris.assertHasAuthority(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected fragment.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://helloworld.org:8080/index.html#print")).hasFragment("print");
   * 
   * // These assertions fail:
   * assertThat(new URI("http://helloworld.org:8080/index.html#print")).hasFragment("hello");
   * assertThat(new URI("http://helloworld.org:8080/index.html")).hasFragment("hello");</code></pre>
   *
   * @param expected the expected fragment of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual fragment is not equal to the expected fragment.
   */
  public S hasFragment(String expected) {
    uris.assertHasFragment(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has no fragment.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoFragment();
   * 
   * // This assertion fail:
   * assertThat(new URI("http://helloworld.org:8080/index.html#print")).hasNoFragment();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a fragment.
   */
  public S hasNoFragment() {
    uris.assertHasFragment(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected query.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://www.helloworld.org/index.html?type=test")).hasQuery("type=test");
   * 
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html?type=test")).hasQuery("type=hello");
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasQuery("type=hello");</code></pre>
   *
   * @param expected the expected query of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual query is not equal to the expected query.
   */
  public S hasQuery(String expected) {
    uris.assertHasQuery(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has no query.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoQuery();
   * 
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html?type=test")).hasNoQuery();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a query.
   */
  public S hasNoQuery() {
    uris.assertHasQuery(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected scheme.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("ftp://helloworld.org")).hasScheme("ftp");
   * 
   * // These assertion fails:
   * assertThat(new URI("http://helloworld.org")).hasScheme("ftp");</code></pre>
   *
   * @param expected the expected scheme of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual scheme is not equal to the expected scheme.
   */
  public S hasScheme(String expected) {
    uris.assertHasScheme(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has the expected userinfo.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://test:pass@www.helloworld.org/index.html")).hasUserInfo("test:pass");
   * assertThat(new URI("http://test@www.helloworld.org/index.html")).hasUserInfo("test");
   * assertThat(new URI("http://:pass@www.helloworld.org/index.html")).hasUserInfo(":pass");
   * 
   * // These assertions fail:
   * assertThat(new URI("http://test:pass@www.helloworld.org/index.html")).hasUserInfo("test:fail");
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasUserInfo("test:pass");</code></pre>
   *
   * @param expected the expected userinfo of the actual {@code URI}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual userinfo is not equal to the expected userinfo.
   */
  public S hasUserInfo(String expected) {
    uris.assertHasUserInfo(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has no userinfo.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoUserInfo();
   * 
   * // This assertion fails:
   * assertThat(new URI("http://test:pass@www.helloworld.org/index.html")).hasNoUserInfo();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has some userinfo.
   */
  public S hasNoUserInfo() {
    uris.assertHasUserInfo(info, actual, null);
    return myself;
  }
}
