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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Objects.deepEquals;
import static org.assertj.core.error.uri.ShouldBeEqualToWithSortedQueryParameters.shouldBeEqualToWithSortedQueryParameters;
import static org.assertj.core.error.uri.ShouldBeReachableHTTP.shouldBeReachableHTTP;
import static org.assertj.core.error.uri.ShouldHaveAnchor.shouldHaveAnchor;
import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

public class Urls {

  private static final Urls INSTANCE = new Urls();

  @VisibleForTesting
  Failures failures = Failures.instance();

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

  public void assertHTTPReachable(final AssertionInfo info, final URL actual) {
    assertNotNull(info, actual);
    try {
      HttpURLConnection con = (HttpURLConnection) actual.openConnection();
      if (con.getResponseCode() != 200) {
        throw failures.failure(info, shouldBeReachableHTTP(actual));
      }
    }
    catch (IOException ignored) {
      throw failures.failure(info, shouldBeReachableHTTP(actual));
    }
  }

  public void assertHasProtocol(final AssertionInfo info, final URL actual, final String protocol) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getProtocol(), protocol)) throw failures.failure(info, shouldHaveProtocol(actual, protocol));
  }

  public void assertHasPath(AssertionInfo info, URL actual, String path) {
    assertNotNull(info, actual);
    checkArgument(path != null, "Expecting given path not to be null");
    if (!Objects.equals(actual.getPath(), path)) throw failures.failure(info, shouldHavePath(actual, path));
  }

  public void assertHasPort(AssertionInfo info, URL actual, int expected) {
    assertNotNull(info, actual);
    if (actual.getPort() != expected) throw failures.failure(info, shouldHavePort(actual, expected));
  }

  public void assertHasHost(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getHost(), expected)) throw failures.failure(info, shouldHaveHost(actual, expected));
  }

  public void assertHasAuthority(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getAuthority(), expected))
      throw failures.failure(info, shouldHaveAuthority(actual, expected));
  }

  public void assertHasQuery(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getQuery(), expected)) throw failures.failure(info, shouldHaveQuery(actual, expected));
  }

  public void assertHasAnchor(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getRef(), expected)) throw failures.failure(info, shouldHaveAnchor(actual, expected));
  }

  public void assertHasUserInfo(AssertionInfo info, URL actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getUserInfo(), expected)) throw failures.failure(info, shouldHaveUserInfo(actual, expected));
  }

  public void assertHasParameter(AssertionInfo info, URL actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());
    if (!parameters.containsKey(name)) throw failures.failure(info, shouldHaveParameter(actual, name));
  }

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

  public void assertHasNoParameters(AssertionInfo info, URL actual) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());
    if (!parameters.isEmpty()) throw failures.failure(info, shouldHaveNoParameters(actual, parameters.keySet()));
  }

  public void assertHasNoParameter(AssertionInfo info, URL actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());
    if (parameters.containsKey(name))
      throw failures.failure(info, shouldHaveNoParameter(actual, name, parameters.get(name)));
  }

  public void assertHasNoParameter(AssertionInfo info, URL actual, String name, String unwantedValue) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);
      if (values.contains(unwantedValue))
        throw failures.failure(info, shouldHaveNoParameter(actual, name, unwantedValue, values));
    }
  }

  public void assertIsEqualToWithSortedQueryParameters(AssertionInfo info, URL actual, URL expected) {
    assertNotNull(info, actual);
    boolean differentNonQueryParams = !extractNonQueryParams(expected).equals(extractNonQueryParams(actual));
    boolean differentSortedQueryParams = !deepEquals(extractSortedQueryParams(expected), extractSortedQueryParams(actual));
    if (differentNonQueryParams || differentSortedQueryParams)
      throw failures.failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }
}
