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
package org.assertj.core.api;

import java.net.URI;

import org.assertj.core.internal.Uris;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link URI}s.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @see java.net.URI
 */
public abstract class AbstractUriAssert<SELF extends AbstractUriAssert<SELF>> extends AbstractAssert<SELF, URI> {

  @VisibleForTesting
  protected Uris uris = Uris.instance();

  public AbstractUriAssert(final URI actual, final Class<?> selfType) {
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
  public SELF hasPath(String expected) {
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
   * // these assertions fail:
   * assertThat(new URI("http://helloworld.org")).hasNoPath(); // empty path
   * assertThat(new URI("http://helloworld.org/france")).hasNoPath();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a path.
   */
  public SELF hasNoPath() {
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
  public SELF hasPort(int expected) {
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
   * // This assertion fails:
   * assertThat(new URI("http://helloworld.org:8080")).hasNoPort();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a port.
   */
  public SELF hasNoPort() {
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
  public SELF hasHost(String expected) {
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
  public SELF hasAuthority(String expected) {
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
  public SELF hasFragment(String expected) {
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
   * // This assertion fails:
   * assertThat(new URI("http://helloworld.org:8080/index.html#print")).hasNoFragment();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a fragment.
   */
  public SELF hasNoFragment() {
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
  public SELF hasQuery(String expected) {
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
  public SELF hasNoQuery() {
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
  public SELF hasScheme(String expected) {
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
  public SELF hasUserInfo(String expected) {
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
  public SELF hasNoUserInfo() {
    uris.assertHasUserInfo(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has a parameter with the expected name.
   * <p>
   * The value of the parameter is not checked.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://www.helloworld.org/index.html?happy")).hasParameter("happy");
   * assertThat(new URI("http://www.helloworld.org/index.html?happy=very")).hasParameter("happy");
   *
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasParameter("happy");
   * assertThat(new URI("http://www.helloworld.org/index.html?sad=much")).hasParameter("happy");</code></pre>
   *
   * @param name the name of the parameter expected to be present.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not have the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   *
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasParameter(String name) {
    uris.assertHasParameter(info, actual, name);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} has a parameter with the expected name and value.
   * <p>
   * Use {@code null} to indicate an absent value (e.g. {@code foo&bar}) as opposed to an empty value (e.g.
   * {@code foo=&bar=}).
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://www.helloworld.org/index.html?happy")).hasParameter("happy", null);
   * assertThat(new URI("http://www.helloworld.org/index.html?happy=very")).hasParameter("happy", "very");
   *
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html?sad")).hasParameter("sad", "much");
   * assertThat(new URI("http://www.helloworld.org/index.html?sad=much")).hasParameter("sad", null);</code></pre>
   *
   * @param name the name of the parameter expected to be present.
   * @param value the value of the parameter expected to be present.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not have the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   *
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasParameter(String name, String value) {
    uris.assertHasParameter(info, actual, name, value);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} does not have any parameters.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoParameters();
   *
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html?sad")).hasNoParameters();
   * assertThat(new URI("http://www.helloworld.org/index.html?sad=much")).hasNoParameters();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   *
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameters() {
    uris.assertHasNoParameters(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} does not have a parameter with the specified name.
   * <p>
   * The value of the parameter is not checked.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoParameter("happy");
   *
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html?sad")).hasNoParameter("sad");
   * assertThat(new URI("http://www.helloworld.org/index.html?sad=much")).hasNoParameter("sad");</code></pre>
   *
   * @param name the name of the parameter expected to be absent.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   */
  public SELF hasNoParameter(String name) {
    uris.assertHasNoParameter(info, actual, name);
    return myself;
  }

  /**
   * Verifies that the actual {@code URI} does not have a parameter with the expected name and value.
   * <p>
   * Use {@code null} to indicate an absent value (e.g. {@code foo&bar}) as opposed to an empty value (e.g.
   * {@code foo=&bar=}).
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoParameter("happy", "very");
   * assertThat(new URI("http://www.helloworld.org/index.html?happy")).hasNoParameter("happy", "very");
   * assertThat(new URI("http://www.helloworld.org/index.html?happy=very")).hasNoParameter("happy", null);
   *
   * // These assertions fail:
   * assertThat(new URI("http://www.helloworld.org/index.html?sad")).hasNoParameter("sad", null);
   * assertThat(new URI("http://www.helloworld.org/index.html?sad=much")).hasNoParameter("sad", "much");</code></pre>
   *
   * @param name the name of the parameter expected to be absent.
   * @param value the value of the parameter expected to be absent.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   */
  public SELF hasNoParameter(String name, String value) {
    uris.assertHasNoParameter(info, actual, name, value);
    return myself;
  }
}
