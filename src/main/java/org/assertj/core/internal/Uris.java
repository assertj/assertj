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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.error.uri.ShouldHaveFragment.shouldHaveFragment;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameters;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveSameParameters;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.error.uri.ShouldHavePort.shouldHavePort;
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;
import static org.assertj.core.error.uri.ShouldHaveScheme.shouldHaveScheme;
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;
import static org.assertj.core.internal.Comparables.assertNotNull;

public class Uris {

  private static final String UTF_8 = "UTF-8";

  private static final String EQUAL = "=";

  private static final String AND = "&";

  private static final Uris INSTANCE = new Uris();

  @VisibleForTesting
  Failures failures = Failures.instance();

  public static Uris instance() {
    return INSTANCE;
  }

  Uris() {}

  public void assertHasScheme(final AssertionInfo info, final URI actual, final String scheme) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getScheme(), scheme)) throw failures.failure(info, shouldHaveScheme(actual, scheme));
  }

  public void assertHasPath(AssertionInfo info, URI actual, String path) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getPath(), path)) throw failures.failure(info, shouldHavePath(actual, path));
  }

  public void assertHasPort(AssertionInfo info, URI actual, Integer expected) {
    assertNotNull(info, actual);
    if (actual.getPort() != expected) throw failures.failure(info, shouldHavePort(actual, expected));
  }

  public void assertHasHost(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getHost(), expected)) throw failures.failure(info, shouldHaveHost(actual, expected));
  }

  public void assertHasAuthority(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getAuthority(), expected))
      throw failures.failure(info, shouldHaveAuthority(actual, expected));
  }

  public void assertHasFragment(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getFragment(), expected)) throw failures.failure(info, shouldHaveFragment(actual, expected));
  }

  public void assertHasQuery(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!Objects.equals(actual.getQuery(), expected)) throw failures.failure(info, shouldHaveQuery(actual, expected));
  }

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

  public void assertHasParameter(AssertionInfo info, URI actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());
    if (!parameters.containsKey(name)) throw failures.failure(info, shouldHaveParameter(actual, name));
  }

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

  public void assertHasNoParameters(AssertionInfo info, URI actual) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());
    if (!parameters.isEmpty()) throw failures.failure(info, shouldHaveNoParameters(actual, parameters.keySet()));
  }

  public void assertHasNoParameter(AssertionInfo info, URI actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());
    if (parameters.containsKey(name))
      throw failures.failure(info, shouldHaveNoParameter(actual, name, parameters.get(name)));
  }

  public void assertHasNoParameter(AssertionInfo info, URI actual, String name, String unwantedValue) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getRawQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);
      if (values.contains(unwantedValue))
        throw failures.failure(info, shouldHaveNoParameter(actual, name, unwantedValue, values));
    }
  }

  public final void assertContainsExactlyParameters(AssertionInfo info, URI actual, Collection<Map.Entry<String, String>> expectedParameters) {
    requireNonNull(expectedParameters, ErrorMessages.setOfEntriesToLookForIsNull());

    final HashMap<String, List<String>> multiValueMap = new HashMap<>();

    expectedParameters.forEach(param ->
      multiValueMap.compute(param.getKey(), (key, values) -> {
        if (values == null) values = new ArrayList<>();
        values.add(param.getValue());
        return values;
      })
    );

    assertContainsExactlyParameters(info, actual, multiValueMap);
  }

  public void assertContainsExactlyParameters(AssertionInfo info, URI actual, Map<String, List<String>> expectedParameters) {
    assertNotNull(info, actual);
    requireNonNull(expectedParameters, ErrorMessages.mapOfEntriesToLookForIsNull());

    Map<String, List<String>> urlParameters = getParameters(actual.getQuery());

    Map<String, List<String>> notExpectedInUrl = new LinkedHashMap<>();
    urlParameters.forEach((key, value) -> notExpectedInUrl.put(key, new ArrayList<>(value)));

    Map<String, List<String>> missingInUrl = new LinkedHashMap<>();
    expectedParameters.forEach((key, value) -> missingInUrl.put(key, new ArrayList<>(value)));

    expectedParameters.forEach((parameterKey, parameterValue) -> {
      final List<String> missingValues = missingInUrl.getOrDefault(parameterKey, Collections.emptyList());
      final List<String> nonExpectedValues = notExpectedInUrl.getOrDefault(parameterKey, Collections.emptyList());

      final List<String> forDeleteFromNonExpectedValues = nonExpectedValues.stream()
        .filter(missingValues::contains)
        .collect(Collectors.toList());

      final List<String> forDeleteFromMissingValues = missingValues.stream()
        .filter(nonExpectedValues::contains)
        .collect(Collectors.toList());

      nonExpectedValues.removeAll(forDeleteFromNonExpectedValues);
      missingValues.removeAll(forDeleteFromMissingValues);

      if (nonExpectedValues.isEmpty()) notExpectedInUrl.remove(parameterKey);
      if (missingValues.isEmpty()) missingInUrl.remove(parameterKey);
    });

    if (!notExpectedInUrl.isEmpty() || !missingInUrl.isEmpty()) {
      throw failures.failure(info, shouldHaveSameParameters(urlParameters, expectedParameters, missingInUrl, notExpectedInUrl));
    }
  }

}
