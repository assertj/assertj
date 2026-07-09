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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.error.uri.ShouldHaveFragment.shouldHaveFragment;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.error.uri.ShouldHaveNoHost.shouldHaveNoHost;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameters;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.error.uri.ShouldHavePort.shouldHavePort;
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;
import static org.assertj.core.error.uri.ShouldHaveScheme.shouldHaveScheme;
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;
import static org.assertj.core.internal.Comparables.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.assertj.core.api.AssertionInfo;

/** Reusable assertions for {@link URI} values. */
public class Uris {

  private static final String UTF_8 = "UTF-8";

  private static final String EQUAL = "=";

  private static final String AND = "&";

  private static final Uris INSTANCE = new Uris();

  private final Failures failures = Failures.instance();

  /**
   * Returns the shared URI assertions.
   *
   * @return the shared instance
   */
  public static Uris instance() {
    return INSTANCE;
  }

  Uris() {}

  /**
   * Verifies the URI scheme.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param scheme the expected scheme
   */
  public void assertHasScheme(final AssertionInfo info, final URI actual, final String scheme) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getScheme(), scheme)) throw failures.failure(info, shouldHaveScheme(actual, scheme));
  }

  /**
   * Verifies the URI path.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param path the expected path
   */
  public void assertHasPath(AssertionInfo info, URI actual, String path) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getPath(), path)) throw failures.failure(info, shouldHavePath(actual, path));
  }

  /**
   * Verifies the URI port.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expected the expected port
   */
  public void assertHasPort(AssertionInfo info, URI actual, Integer expected) {
    assertNotNull(info, actual);
    if (actual.getPort() != expected) throw failures.failure(info, shouldHavePort(actual, expected));
  }

  /**
   * Verifies the URI host.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expected the expected host
   */
  public void assertHasHost(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    requireNonNull(expected, "The expected host should not be null");
    if (!Objects.equals(actual.getHost(), expected)) throw failures.failure(info, shouldHaveHost(actual, expected));
  }

  /**
   * Verifies that the URI has no host.
   *
   * @param info assertion information
   * @param actual the actual URI
   */
  public void assertHasNoHost(AssertionInfo info, URI actual) {
    assertNotNull(info, actual);
    if (actual.getHost() != null) throw failures.failure(info, shouldHaveNoHost(actual));
  }

  /**
   * Verifies the URI authority.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expected the expected authority
   */
  public void assertHasAuthority(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getAuthority(), expected))
      throw failures.failure(info, shouldHaveAuthority(actual, expected));
  }

  /**
   * Verifies the URI fragment.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expected the expected fragment
   */
  public void assertHasFragment(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getFragment(), expected)) throw failures.failure(info, shouldHaveFragment(actual, expected));
  }

  /**
   * Verifies the URI query.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expected the expected query
   */
  public void assertHasQuery(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getQuery(), expected)) throw failures.failure(info, shouldHaveQuery(actual, expected));
  }

  /**
   * Verifies the URI user information.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expected the expected user information
   */
  public void assertHasUserInfo(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getUserInfo(), expected)) throw failures.failure(info, shouldHaveUserInfo(actual, expected));
  }

  static Map<String, List<String>> getParameters(String query) {
    Map<String, List<String>> parameters = new LinkedHashMap<>();

    if (query != null && !query.isEmpty()) {
      for (String pair : query.split(AND)) {
        int equalIndex = pair.indexOf(EQUAL);
        String key = equalIndex == -1 ? pair : pair.substring(0, equalIndex);
        String value = equalIndex == -1 ? null : pair.substring(equalIndex + 1);

        try {
          key = URLDecoder.decode(key, UTF_8);
        } catch (UnsupportedEncodingException ex) {
          // UTF-8 is missing? Allow the key to remain encoded (no reasonable alternative).
        }

        if (value != null) {
          try {
            value = URLDecoder.decode(value, UTF_8);
          } catch (UnsupportedEncodingException ex) {
            // UTF-8 is missing? Allow the value to remain encoded (no reasonable alternative).
          }
        }

        if (!parameters.containsKey(key)) {
          parameters.put(key, new ArrayList<>());
        }

        parameters.get(key).add(value);
      }
    }

    return parameters;
  }

  /**
   * Verifies that the URI has a named query parameter.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param name the parameter name
   */
  public void assertHasParameter(AssertionInfo info, URI actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());
    if (!parameters.containsKey(name)) throw failures.failure(info, shouldHaveParameter(actual, name));
  }

  /**
   * Verifies that the URI has a query parameter with the expected value.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param expectedParameterName the parameter name
   * @param expectedParameterValue the parameter value
   */
  public void assertHasParameter(AssertionInfo info, URI actual, String expectedParameterName,
                                 String expectedParameterValue) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());

    if (!parameters.containsKey(expectedParameterName))
      throw failures.failure(info, shouldHaveParameter(actual, expectedParameterName, expectedParameterValue));

    List<String> values = parameters.get(expectedParameterName);
    if (!values.contains(expectedParameterValue))
      throw failures.failure(info, shouldHaveParameter(actual, expectedParameterName, expectedParameterValue, values));
  }

  /**
   * Verifies that the URI has no query parameters.
   *
   * @param info assertion information
   * @param actual the actual URI
   */
  public void assertHasNoParameters(AssertionInfo info, URI actual) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());
    if (!parameters.isEmpty()) throw failures.failure(info, shouldHaveNoParameters(actual, parameters.keySet()));
  }

  /**
   * Verifies that the URI has no query parameter with the given name.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param name the prohibited parameter name
   */
  public void assertHasNoParameter(AssertionInfo info, URI actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());
    if (parameters.containsKey(name))
      throw failures.failure(info, shouldHaveNoParameter(actual, name, parameters.get(name)));
  }

  /**
   * Verifies that the URI has no query parameter with the given value.
   *
   * @param info assertion information
   * @param actual the actual URI
   * @param name the parameter name
   * @param unwantedValue the prohibited parameter value
   */
  public void assertHasNoParameter(AssertionInfo info, URI actual, String name, String unwantedValue) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);
      if (values.contains(unwantedValue))
        throw failures.failure(info, shouldHaveNoParameter(actual, name, unwantedValue, values));
    }
  }

}
