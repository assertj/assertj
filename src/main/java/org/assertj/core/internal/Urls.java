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
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.UrlsException;
import org.assertj.core.util.VisibleForTesting;

import java.net.URISyntaxException;
import java.net.URL;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.internal.Comparables.assertNotNull;

/**
 * Core assertion class for {@link java.net.URL} assertions
 */
public class Urls {

  private static final Urls INSTANCE = new Urls();

  @VisibleForTesting
  Failures failures = Failures.instance();

  public static Urls instance() {
	return INSTANCE;
  }

  Urls() {
  }

  public void assertHasScheme(final AssertionInfo info, final URL actual, final String expected) {
	assertNotNull(info, actual);

  try {
    if (!actual.toURI().getScheme().equals(expected))
        throw failures.failure(info, shouldBeEqual(actual, expected, info.representation()));
    }catch(URISyntaxException e){
      throw new UrlsException(format("Unable to parse URI reference:<%s>", actual), e);
  }
  }

  public void assertHasPathEquals(AssertionInfo info, URL actual, String expected) {
  assertNotNull(info, actual);

  try {
    if (!actual.toURI().getPath().equals(expected))
        throw failures.failure(info, shouldBeEqual(actual, expected, info.representation()));
    }catch(URISyntaxException e){
        throw new UrlsException(format("Unable to parse URI reference:<%s>", actual), e);
    }
  }
}
