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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.error.uri.ShouldHaveFragment.shouldHaveFragment;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.error.uri.ShouldHavePort.shouldHavePort;
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;
import static org.assertj.core.error.uri.ShouldHaveScheme.shouldHaveScheme;
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;
import static org.assertj.core.internal.Comparables.assertNotNull;
import static org.assertj.core.util.Objects.areEqual;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

public class Uris {

  private static final Uris INSTANCE = new Uris();

  @VisibleForTesting
  Failures failures = Failures.instance();

  public static Uris instance() {
    return INSTANCE;
  }

  Uris() {}

  public void assertHasScheme(final AssertionInfo info, final URI actual, final String scheme) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getScheme(), scheme)) throw failures.failure(info, shouldHaveScheme(actual, scheme));
  }

  public void assertHasPath(AssertionInfo info, URI actual, String path) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getPath(), path)) throw failures.failure(info, shouldHavePath(actual, path));
  }

  public void assertHasPort(AssertionInfo info, URI actual, Integer expected) {
    assertNotNull(info, actual);
    if (actual.getPort() != expected) throw failures.failure(info, shouldHavePort(actual, expected));
  }

  public void assertHasHost(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getHost(), expected)) throw failures.failure(info, shouldHaveHost(actual, expected));
  }

  public void assertHasAuthority(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getAuthority(), expected))
      throw failures.failure(info, shouldHaveAuthority(actual, expected));
  }

  public void assertHasFragment(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getFragment(), expected)) throw failures.failure(info, shouldHaveFragment(actual, expected));
  }

  public void assertHasQuery(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getQuery(), expected)) throw failures.failure(info, shouldHaveQuery(actual, expected));
  }

  public void assertHasUserInfo(AssertionInfo info, URI actual, String expected) {
    assertNotNull(info, actual);
    if (!areEqual(actual.getUserInfo(), expected)) throw failures.failure(info, shouldHaveUserInfo(actual, expected));
  }

  @VisibleForTesting
  public static Map<String, List<String>> getParameters(String query) {
    Map<String, List<String>> parameters = new HashMap<String, List<String>>();

    if (query != null && !query.isEmpty()) {
      for (String pair : query.split("&")) {
        int equalSign = pair.indexOf("=");
        String key = equalSign == -1 ? pair : pair.substring(0, equalSign);
        String value = equalSign == -1 ? null : pair.substring(equalSign + 1);

        try {
          key = URLDecoder.decode(key, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
          // UTF-8 is missing? Allow the key to remain encoded (no reasonable alternative).
        }

        if (value != null) {
          try {
            value = URLDecoder.decode(value, "UTF-8");
          } catch (UnsupportedEncodingException ex) {
            // UTF-8 is missing? Allow the value to remain encoded (no reasonable alternative).
          }
        }

        if (!parameters.containsKey(key)) {
          parameters.put(key, new ArrayList<String>());
        }

        parameters.get(key).add(value);
      }
    }

    return parameters;
  }

  public void assertHasParameter(AssertionInfo info, URI actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (!parameters.containsKey(name)) {
      throw failures.failure(info, shouldHaveParameter(actual, name));
    }
  }

  public void assertHasParameter(AssertionInfo info, URI actual, String name, String value) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (!parameters.containsKey(name)) {
      throw failures.failure(info, shouldHaveParameter(actual, name, value));
    }

    List<String> values = parameters.get(name);

    if (!values.contains(value)) {
      if (values.size() == 1) {
        throw failures.failure(info, shouldHaveParameter(actual, name, value, values.get(0)));
      }

      throw failures.failure(info, shouldHaveParameter(actual, name, value, values.toString()));
    }
  }

  public void assertHasNoParameter(AssertionInfo info, URI actual, String name) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);

      if (values.size() == 1) {
        throw failures.failure(info, shouldHaveNoParameter(actual, name, values.get(0)));
      }

      throw failures.failure(info, shouldHaveNoParameter(actual, name, values.toString()));
    }
  }

  public void assertHasNoParameter(AssertionInfo info, URI actual, String name, String value) {
    assertNotNull(info, actual);

    Map<String, List<String>> parameters = getParameters(actual.getQuery());

    if (parameters.containsKey(name)) {
      List<String> values = parameters.get(name);

      if (values.contains(value)) {
        throw failures.failure(info, shouldHaveNoParameter(actual, name, value, values.get(0)));
      }
    }
  }
}
