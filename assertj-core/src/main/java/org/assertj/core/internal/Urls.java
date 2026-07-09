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
package org.assertj.core.internal;

import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
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
import static org.assertj.core.internal.Comparables.assertNotNull;
import static org.assertj.core.internal.Uris.getParameters;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.assertj.core.api.AssertionInfo;

/** Reusable assertions for {@link URL} values. */
public class Urls {

  private static final Urls INSTANCE = new Urls();

  private final Failures failures = Failures.instance();

  /**
   * Returns the shared URL assertions.
   *
   * @return the shared instance
   */
  public static Urls instance() {
    return INSTANCE;
  }

  Urls() {}

  private static String extractNonQueryParams(URL url) {
    String queryPart = url.getQuery() == null ? "" : url.getQuery();
    return url.toString().replace(queryPart, " ");
  }

  private static String[] extractSortedQueryParams(URL url) {
    String[] queryParams = (url.getQuery() == null ? "" : url.getQuery()).split("&");
    Arrays.sort(queryParams);
    return queryParams;
  }

  /**
   * Verifies the URL protocol.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param protocol the expected protocol
   */
  public void assertHasProtocol(final AssertionInfo info, final URL actual, final String protocol) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getProtocol(), protocol)) throw failures.failure(info, shouldHaveProtocol(actual, protocol));
  }

  /**
   * Verifies the URL path.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param path the expected path
   */
  public void assertHasPath(AssertionInfo info, URL actual, String path) {
    assertNotNull(info, actual);
    checkArgument(path != null, "Expecting given path not to be null");
    if (!Objects.equals(actual.getPath(), path)) throw failures.failure(info, shouldHavePath(actual, path));
  }

  /**
   * Verifies the URL port.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected port
   */
  public void assertHasPort(AssertionInfo info, URL actual, int expected) {
    assertNotNull(info, actual);
    if (actual.getPort() != expected) throw failures.failure(info, shouldHavePort(actual, expected));
  }

  /**
   * Verifies the URL host.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected host
   */
  public void assertHasHost(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    requireNonNull(expected, "The expected host should not be null");
    if (!Objects.equals(actual.getHost(), expected)) throw failures.failure(info, shouldHaveHost(actual, expected));
  }

  /**
   * Verifies that the URL has no host.
   *
   * @param info assertion information
   * @param actual the actual URL
   */
  public void assertHasNoHost(AssertionInfo info, URL actual) {
    assertNotNull(info, actual);
    if (actual.getHost() != null && !actual.getHost().isEmpty()) throw failures.failure(info, shouldHaveNoHost(actual));
  }

  /**
   * Verifies the URL authority.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected authority
   */
  public void assertHasAuthority(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getAuthority(), expected))
      throw failures.failure(info, shouldHaveAuthority(actual, expected));
  }

  /**
   * Verifies the URL query.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected query
   */
  public void assertHasQuery(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getQuery(), expected)) throw failures.failure(info, shouldHaveQuery(actual, expected));
  }

  /**
   * Verifies the URL anchor.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected anchor
   */
  public void assertHasAnchor(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getRef(), expected)) throw failures.failure(info, shouldHaveAnchor(actual, expected));
  }

  /**
   * Verifies the URL user information.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected user information
   */
  public void assertHasUserInfo(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getUserInfo(), expected)) throw failures.failure(info, shouldHaveUserInfo(actual, expected));
  }

  /**
   * Verifies that the URL has a named query parameter.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param name the parameter name
   */
  public void assertHasParameter(AssertionInfo info, URL actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());
    if (!parameters.containsKey(name)) throw failures.failure(info, shouldHaveParameter(actual, name));
  }

  /**
   * Verifies that the URL has a query parameter with the expected value.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expectedParameterName the parameter name
   * @param expectedParameterValue the parameter value
   */
  public void assertHasParameter(AssertionInfo info, URL actual, String expectedParameterName,
                                 String expectedParameterValue) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (!parameters.containsKey(expectedParameterName))
      throw failures.failure(info, shouldHaveParameter(actual, expectedParameterName, expectedParameterValue));

    List<String> values = parameters.get(expectedParameterName);
    if (!values.contains(expectedParameterValue))
      throw failures.failure(info, shouldHaveParameter(actual, expectedParameterName, expectedParameterValue, values));
  }

  /**
   * Verifies that the URL has no query parameters.
   *
   * @param info assertion information
   * @param actual the actual URL
   */
  public void assertHasNoParameters(AssertionInfo info, URL actual) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());
    if (!parameters.isEmpty()) throw failures.failure(info, shouldHaveNoParameters(actual, parameters.keySet()));
  }

  /**
   * Verifies that the URL has no query parameter with the given name.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param name the prohibited parameter name
   */
  public void assertHasNoParameter(AssertionInfo info, URL actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());
    if (parameters.containsKey(name))
      throw failures.failure(info, shouldHaveNoParameter(actual, name, parameters.get(name)));
  }

  /**
   * Verifies that the URL has no query parameter with the given value.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param name the parameter name
   * @param unwantedValue the prohibited parameter value
   */
  public void assertHasNoParameter(AssertionInfo info, URL actual, String name, String unwantedValue) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);
      if (values.contains(unwantedValue))
        throw failures.failure(info, shouldHaveNoParameter(actual, name, unwantedValue, values));
    }
  }

  /**
   * Verifies URL equality after sorting query parameters.
   *
   * @param info assertion information
   * @param actual the actual URL
   * @param expected the expected URL
   */
  public void assertIsEqualToWithSortedQueryParameters(AssertionInfo info, URL actual, URL expected) {
    assertNotNull(info, actual);
    boolean differentNonQueryParams = !extractNonQueryParams(expected).equals(extractNonQueryParams(actual));
    boolean differentSortedQueryParams = !deepEquals(extractSortedQueryParams(expected), extractSortedQueryParams(actual));
    if (differentNonQueryParams || differentSortedQueryParams)
      throw failures.failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }
}
