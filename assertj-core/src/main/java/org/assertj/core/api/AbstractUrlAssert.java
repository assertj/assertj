/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.UrlQueryParser.parseParameters;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.uri.ShouldBeEqualToWithSortedQueryParameters.shouldBeEqualToWithSortedQueryParameters;
import static org.assertj.core.error.uri.ShouldHaveAnchor.shouldHaveAnchor;
import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.error.uri.ShouldHaveNoHost.shouldHaveNoHost;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameters;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.error.uri.ShouldHavePort.shouldHavePort;
import static org.assertj.core.error.uri.ShouldHaveProtocol.shouldHaveProtocol;
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for all implementations of assertions for {@link URL}s.
 *
 * @param <SELF> the "self" type of this assertion class.
 */
public abstract class AbstractUrlAssert<SELF extends AbstractUrlAssert<SELF>> extends AbstractAssert<SELF, URL> {

  protected AbstractUrlAssert(URL actual, Class<?> selfType) {
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
    isNotNull();
    assertHasProtocol(expected);
    return myself;
  }

  private void assertHasProtocol(String expected) {
    if (!Objects.equals(actual.getProtocol(), expected)) throw assertionError(shouldHaveProtocol(actual, expected));
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
   * // this assertion throws a NullPointerException:
   * assertThat(new URL("http://helloworld.org/pages")).hasPath(null);</code></pre>
   *
   * @param expected the expected path of the actual {@code URL}.
   * @return {@code this} assertion object.
   * @throws AssertionError       if the actual URL path is not equal to the expected path.
   * @throws NullPointerException if the expected path is {@code null}.
   */
  public SELF hasPath(String expected) {
    isNotNull();
    assertHasPath(expected);
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
   * @throws AssertionError if {@code actual} has a path.
   */
  public SELF hasNoPath() {
    isNotNull();
    assertHasPath("");
    return myself;
  }

  private void assertHasPath(String expected) {
    requireNonNull(expected, shouldNotBeNull("expected")::create);
    if (!Objects.equals(actual.getPath(), expected)) throw assertionError(shouldHavePath(actual, expected));
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
    isNotNull();
    assertHasPort(expected);
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
   * @throws AssertionError if {@code actual} has a port.
   */
  public SELF hasNoPort() {
    isNotNull();
    assertHasPort(-1);
    return myself;
  }

  private void assertHasPort(int expected) {
    if (actual.getPort() != expected) throw assertionError(shouldHavePort(actual, expected));
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
    isNotNull();
    assertHasHost(expected);
    return myself;
  }

  private void assertHasHost(String expected) {
    requireNonNull(expected, "The expected host should not be null");
    if (!Objects.equals(actual.getHost(), expected)) throw assertionError(shouldHaveHost(actual, expected));
  }

  /**
   * Verifies that the actual {@code URL} has no host.
   * <p>
   * Examples:
   * <pre><code class='java'> // This assertion succeeds:
   * assertThat(new URL("file:///home/user/Documents/hello-world.txt")).hasNoHost();
   *
   * // This assertion fails:
   * assertThat(new URL("http://helloworld.org:8080/index.html")).hasNoHost();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if actual has a host.
   * @since 3.22.0
   */
  public SELF hasNoHost() {
    isNotNull();
    assertHasNoHost();
    return myself;
  }

  private void assertHasNoHost() {
    if (actual.getHost() != null && !actual.getHost().isEmpty()) throw assertionError(shouldHaveNoHost(actual));
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
    isNotNull();
    assertHasAuthority(expected);
    return myself;
  }

  private void assertHasAuthority(String expected) {
    if (!Objects.equals(actual.getAuthority(), expected)) throw assertionError(shouldHaveAuthority(actual, expected));
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
    isNotNull();
    assertHasQuery(expected);
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
   * @throws AssertionError if {@code actual} has a query.
   */
  public SELF hasNoQuery() {
    isNotNull();
    assertHasQuery(null);
    return myself;
  }

  private void assertHasQuery(String expected) {
    if (!Objects.equals(actual.getQuery(), expected)) throw assertionError(shouldHaveQuery(actual, expected));
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
    isNotNull();
    assertHasAnchor(expected);
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
   * @throws AssertionError if {@code actual} has a anchor.
   */
  public SELF hasNoAnchor() {
    isNotNull();
    assertHasAnchor(null);
    return myself;
  }

  private void assertHasAnchor(String expected) {
    if (!Objects.equals(actual.getRef(), expected)) throw assertionError(shouldHaveAnchor(actual, expected));
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
    isNotNull();
    assertHasUserInfo(expected);
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
   * @throws AssertionError if {@code actual} has some userinfo.
   */
  public SELF hasNoUserInfo() {
    isNotNull();
    assertHasUserInfo(null);
    return myself;
  }

  private void assertHasUserInfo(String expected) {
    if (!Objects.equals(actual.getUserInfo(), expected)) throw assertionError(shouldHaveUserInfo(actual, expected));
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
   * @throws AssertionError           if the actual does not have the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   */
  public SELF hasParameter(String name) {
    isNotNull();
    assertHasParameter(name);
    return myself;
  }

  private void assertHasParameter(String name) {
    Map<String, List<String>> parameters = parseParameters(actual.getQuery());
    if (!parameters.containsKey(name)) throw assertionError(shouldHaveParameter(actual, name));
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
   * @param name  the name of the parameter expected to be present.
   * @param value the value of the parameter expected to be present.
   * @return {@code this} assertion object.
   * @throws AssertionError           if the actual does not have the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   */
  public SELF hasParameter(String name, String value) {
    isNotNull();
    assertHasParameter(name, value);
    return myself;
  }

  private void assertHasParameter(String name, String value) {
    Map<String, List<String>> parameters = parseParameters(actual.getQuery());

    if (!parameters.containsKey(name)) throw assertionError(shouldHaveParameter(actual, name, value));

    List<String> values = parameters.get(name);
    if (!values.contains(value)) throw assertionError(shouldHaveParameter(actual, name, value, values));
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
   * @throws AssertionError           if {@code actual} has a parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameters() {
    isNotNull();
    assertHasNoParameters();
    return myself;
  }

  private void assertHasNoParameters() {
    Map<String, List<String>> parameters = parseParameters(actual.getQuery());
    if (!parameters.isEmpty()) throw assertionError(shouldHaveNoParameters(actual, parameters.keySet()));
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
   * @throws AssertionError           if {@code actual} has the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameter(String name) {
    isNotNull();
    assertHasNoParameter(name);
    return myself;
  }

  private void assertHasNoParameter(String name) {
    Map<String, List<String>> parameters = parseParameters(actual.getQuery());
    if (parameters.containsKey(name))
      throw assertionError(shouldHaveNoParameter(actual, name, parameters.get(name)));
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
   * @param name  the name of the parameter expected to be absent.
   * @param value the value of the parameter expected to be absent.
   * @return {@code this} assertion object.
   * @throws AssertionError           if {@code actual} has the expected parameter.
   * @throws IllegalArgumentException if the query string contains an invalid escape sequence.
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoParameter(String name, String value) {
    isNotNull();
    assertHasNoParameter(name, value);
    return myself;
  }

  private void assertHasNoParameter(String name, String unwantedValue) {
    Map<String, List<String>> parameters = parseParameters(actual.getQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);
      if (values.contains(unwantedValue))
        throw assertionError(shouldHaveNoParameter(actual, name, unwantedValue, values));
    }
  }

  /**
   * Verifies that the actual {@code URL} is equivalent to the given one after <b>their parameters are sorted</b>.
   * <p>
   * Example:
   * <pre><code class='java'> URL url = new URL("http://example.com?a=b&amp;c=d");
   *
   * // this assertion succeeds ...
   * assertThat(url).isEqualToWithSortedQueryParameters(new URL("http://example.com?c=d&amp;a=b"))
   *                .isEqualToWithSortedQueryParameters(new URL("http://example.com?a=b&amp;c=d"));
   *
   * // ... but this one fails as parameters do not match.
   * assertThat(url).isEqualToWithSortedQueryParameters(new URL("http://example.com?a=b&amp;c=e"));
   *
   * //... and this one fails as domains are different.
   * assertThat(url).isEqualToWithSortedQueryParameters(new URL("http://example2.com?amp;a=b&amp;c=d")); </code></pre>
   *
   * @param expected the expected {@code URL} to compare actual to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given URL is {@code null}.
   * @throws AssertionError       if the actual {@code URL} is {@code null}.
   * @throws AssertionError       if the actual {@code URL} is not equivalent to the given one after their parameters are sorted.
   * @since 3.16.0
   */
  public SELF isEqualToWithSortedQueryParameters(URL expected) {
    isNotNull();
    assertIsEqualToWithSortedQueryParameters(expected);
    return myself;
  }

  private void assertIsEqualToWithSortedQueryParameters(URL expected) {
    boolean differentNonQueryParams = !extractNonQueryParams(expected).equals(extractNonQueryParams(actual));
    boolean differentSortedQueryParams = !deepEquals(extractSortedQueryParams(expected), extractSortedQueryParams(actual));
    if (differentNonQueryParams || differentSortedQueryParams)
      throw assertionError(shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }

  private static String extractNonQueryParams(URL url) {
    String queryPart = url.getQuery() == null ? "" : url.getQuery();
    return url.toString().replace(queryPart, " ");
  }

  private static String[] extractSortedQueryParams(URL url) {
    String[] queryParams = (url.getQuery() == null ? "" : url.getQuery()).split("&");
    Arrays.sort(queryParams);
    return queryParams;
  }

  /// Returns an [AbstractMapAssert] to make assertions on the decoded query parameters of the actual `URL`
  ///
  /// The map is keyed by parameter name, in query order, and holds all the values of each parameter since a parameter
  /// can be repeated. A parameter without value (e.g. `foo` in `?foo&bar=`) has a `null` value, as
  /// opposed to an empty value (e.g. `bar`).
  ///
  /// Examples:
  ///
  /// ```java
  /// URL url = URI.create("http://www.helloworld.org/index.html?lang=en&tag=java&tag=test").toURL();
  ///
  /// // These assertions succeed:
  /// assertThat(url).parameters()
  ///                .containsOnly(entry("lang", List.of("en")),
  ///                              entry("tag", List.of("java", "test")));
  /// assertThat(url).parameters()
  ///                .containsOnlyKeys("tag", "lang");
  ///
  /// // These assertions fail:
  /// assertThat(url).parameters()
  ///                .containsOnly(entry("lang", List.of("en")));
  /// assertThat(url).parameters()
  ///                .containsEntry("tag", List.of("test", "java"));
  /// ```
  ///
  /// @return a new [AbstractMapAssert] instance for assertions chaining on the query parameters.
  /// @throws AssertionError if `actual` is `null`.
  /// @throws IllegalArgumentException if the query string contains an invalid escape sequence.
  /// @since 3.28.0
  public AbstractMapAssert<?, ?, String, List<String>> parameters() {
    isNotNull();
    return assertThat(parseParameters(actual.getQuery())).withAssertionState(myself);
  }

}
