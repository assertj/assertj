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
package org.assertj.core.internal.urls;

import org.assertj.core.internal.UrlsBaseTest;
import org.assertj.core.util.UrlsException;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * Tests for <code>{@link org.assertj.core.internal.Urls#assertHasScheme(org.assertj.core.api.AssertionInfo, java.net.URL, String)}</code>.
 *
 * @author Alexander Bischof
 */
public class Urls_assertHasScheme_Test extends UrlsBaseTest {
  
  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	urls.assertHasScheme(info, null, "http");
  }

  @Test
  public void should_pass_if_actual_url_has_the_given_scheme() throws MalformedURLException {
      urls.assertHasScheme(info, new URL("http://example.com/pages/"), "http");
  }

  @Test
  public void should_throw_error_if_uri_is_not_malformed() throws MalformedURLException {
      thrown.expect(MalformedURLException.class);
      urls.assertHasScheme(info, new URL("helloworld"), "http");
  }

  @Test
  public void should_throw_error_if_urisyntax_is_not_valid() throws MalformedURLException {
      thrown.expect(UrlsException.class, "http://finance.yahoo.com/q/h?s=^IXIC");
      urls.assertHasScheme(info, new URL("http://finance.yahoo.com/q/h?s=^IXIC"), "http");
  }
}
