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

import java.net.URL;

import org.assertj.core.internal.Urls;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link URL}s.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @see java.net.URL
 */
public abstract class AbstractUrlAssert<SELF extends AbstractUrlAssert<SELF>> extends AbstractAssert<SELF, URL> {

  @VisibleForTesting
  protected Urls urls = Urls.instance();

  public AbstractUrlAssert(final URL actual, final Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code URL} has the expected protocol.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("ftp://helloworld.org")).hasProtocol("ftp");
   *
   * // This assertion fails:
   * assertThat(new URL("http://helloworld.org")).hasProtocol("ftp");</code></pre>
   *
   * @param expected the expected protocol of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual protocol is not equal to the expected protocol.
   */
  public SELF hasProtocol(String expected) {
    urls.assertHasProtocol(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected path (which must not be null).
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://helloworld.org/pages")).hasPath("/pages");
   * assertThat(new URL("http://www.helloworld.org")).hasPath("");
   * // or preferably:
   * assertThat(new URL("http://www.helloworld.org")).hasNoPath();
   *
   * // this assertion fails:
   * assertThat(new URL("http://helloworld.org/pickme")).hasPath("/pages/");
   *
   * // this assertion throws an IllegalArgumentException:
   * assertThat(new URL("http://helloworld.org/pages")).hasPath(null);</code></pre>
   *
   * @param expected the expected path of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual URL path is not equal to the expected path.
   * @throws IllegalArgumentException if given path is null.
   */
  public SELF hasPath(String expected) {
    urls.assertHasPath(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has no path.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org")).hasNoPath();
   *
   * // this assertion fails:
   * assertThat(new URL("http://helloworld.org/france")).hasNoPath();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a path.
   */
  public SELF hasNoPath() {
    urls.assertHasPath(info, actual, "");
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected port.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://helloworld.org:8080")).hasPort(8080);
   *
   * // These assertions fail:
   * assertThat(new URL("http://helloworld.org:8080")).hasPort(9876);
   * assertThat(new URL("http://helloworld.org")).hasPort(8080);</code></pre>
   *
   * @param expected the expected port of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual port is not equal to the expected port.
   */
  public SELF hasPort(int expected) {
    urls.assertHasPort(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has no port.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://helloworld.org")).hasNoPort();
   *
   * // This assertion fails:
   * assertThat(new URL("http://helloworld.org:8080")).hasNoPort();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a port.
   */
  public SELF hasNoPort() {
    urls.assertHasPort(info, actual, -1);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected host.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://helloworld.org/pages")).hasHost("helloworld.org");
   * assertThat(new URL("http://helloworld.org:8080")).hasHost("helloworld.org");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org")).hasHost("helloworld.org");
   * assertThat(new URL("http://www.helloworld.org:8080")).hasHost("helloworld.org");</code></pre>
   *
   * @param expected the expected host of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual host is not equal to the expected host.
   */
  public SELF hasHost(String expected) {
    urls.assertHasHost(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected authority.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://helloworld.org")).hasAuthority("helloworld.org");
   * assertThat(new URL("http://helloworld.org:8080")).hasAuthority("helloworld.org:8080");
   * assertThat(new URL("http://www.helloworld.org:8080/news")).hasAuthority("www.helloworld.org:8080");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org:8080")).hasAuthority("www.helloworld.org");
   * assertThat(new URL("http://www.helloworld.org")).hasAuthority("www.helloworld.org:8080");</code></pre>
   *
   * @param expected the expected authority of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual authority is not equal to the expected authority.
   */
  public SELF hasAuthority(String expected) {
    urls.assertHasAuthority(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected query.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/index.html?type=test")).hasQuery("type=test");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html?type=test")).hasQuery("type=hello");
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasQuery("type=hello");</code></pre>
   *
   * @param expected the expected query of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual query is not equal to the expected query.
   */
  public SELF hasQuery(String expected) {
    urls.assertHasQuery(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has no query.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasNoQuery();
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html?type=test")).hasNoQuery();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a query.
   */
  public SELF hasNoQuery() {
    urls.assertHasQuery(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected anchor.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/news.html#sport")).hasAnchor("sport");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/news.html#sport")).hasAnchor("war");
   * assertThat(new URL("http://www.helloworld.org/news.html")).hasAnchor("sport");</code></pre>
   *
   * @param expected the expected anchor of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual anchor is not equal to the expected anchor.
   */
  public SELF hasAnchor(String expected) {
    urls.assertHasAnchor(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has no anchor.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/news.html")).hasNoAnchor();
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/news.html#sport")).hasNoAnchor();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a anchor.
   */
  public SELF hasNoAnchor() {
    urls.assertHasAnchor(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has the expected userinfo.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://test:pass@www.helloworld.org/index.html")).hasUserInfo("test:pass");
   * assertThat(new URL("http://test@www.helloworld.org/index.html")).hasUserInfo("test");
   * assertThat(new URL("http://:pass@www.helloworld.org/index.html")).hasUserInfo(":pass");
   *
   * // These assertions fail:
   * assertThat(new URL("http://test:pass@www.helloworld.org/index.html")).hasUserInfo("test:fail");
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasUserInfo("test:pass");</code></pre>
   *
   * @param expected the expected userinfo of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual userinfo is not equal to the expected userinfo.
   */
  public SELF hasUserInfo(String expected) {
    urls.assertHasUserInfo(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has no userinfo.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasNoUserInfo();
   *
   * // This assertion fails:
   * assertThat(new URL("http://test:pass@www.helloworld.org/index.html")).hasNoUserInfo();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has some userinfo.
   */
  public SELF hasNoUserInfo() {
    urls.assertHasUserInfo(info, actual, null);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has a parameter with the expected name.
   * <p>
   * The value of the parameter is not checked.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://www.helloworld.org/index.html?happy")).hasParameter("happy");
   * assertThat(new URL("http://www.helloworld.org/index.html?happy=very")).hasParameter("happy");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasParameter("happy");
   * assertThat(new URL("http://www.helloworld.org/index.html?sad=much")).hasParameter("happy");</code></pre>
   *
   * @param name the name of the parameter expected to be present.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not have the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   */
  public SELF hasParameter(String name) {
    urls.assertHasParameter(info, actual, name);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} has a parameter with the expected name and value.
   * <p>
   * Use {@code null} to indicate an absent value (e.g. {@code foo&bar}) as opposed to an empty value (e.g.
   * {@code foo=&bar=}).
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://www.helloworld.org/index.html?happy")).hasParameter("happy", null);
   * assertThat(new URL("http://www.helloworld.org/index.html?happy=very")).hasParameter("happy", "very");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html?sad")).hasParameter("sad", "much");
   * assertThat(new URL("http://www.helloworld.org/index.html?sad=much")).hasParameter("sad", null);</code></pre>
   *
   * @param name the name of the parameter expected to be present.
   * @param value the value of the parameter expected to be present.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not have the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   */
  public SELF hasParameter(String name, String value) {
    urls.assertHasParameter(info, actual, name, value);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} does not have any parameters.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasNoParameters();
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html?sad")).hasNoParameters();
   * assertThat(new URL("http://www.helloworld.org/index.html?sad=much")).hasNoParameters();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has a parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   *
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameters() {
    urls.assertHasNoParameters(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} does not have a parameter with the specified name.
   * <p>
   * The value of the parameter is not checked.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasNoParameter("happy");
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html?sad")).hasNoParameter("sad");
   * assertThat(new URL("http://www.helloworld.org/index.html?sad=much")).hasNoParameter("sad");</code></pre>
   *
   * @param name the name of the parameter expected to be absent.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   *
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameter(String name) {
    urls.assertHasNoParameter(info, actual, name);
    return myself;
  }

  /**
   * Verifies that the actual {@code URL} does not have a parameter with the expected name and value.
   * <p>
   * Use {@code null} to indicate an absent value (e.g. {@code foo&bar}) as opposed to an empty value (e.g.
   * {@code foo=&bar=}).
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://www.helloworld.org/index.html")).hasNoParameter("happy", "very");
   * assertThat(new URL("http://www.helloworld.org/index.html?happy")).hasNoParameter("happy", "very");
   * assertThat(new URL("http://www.helloworld.org/index.html?happy=very")).hasNoParameter("happy", null);
   *
   * // These assertions fail:
   * assertThat(new URL("http://www.helloworld.org/index.html?sad")).hasNoParameter("sad", null);
   * assertThat(new URL("http://www.helloworld.org/index.html?sad=much")).hasNoParameter("sad", "much");</code></pre>
   *
   * @param name the name of the parameter expected to be absent.
   * @param value the value of the parameter expected to be absent.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual has the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   *
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameter(String name, String value) {
    urls.assertHasNoParameter(info, actual, name, value);
    return myself;
  }
}
