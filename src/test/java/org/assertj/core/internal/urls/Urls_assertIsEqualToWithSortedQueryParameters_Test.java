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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.error.uri.ShouldBeEqualToWithSortedQueryParameters.shouldBeEqualToWithSortedQueryParameters;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.net.URL;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Urls#assertIsEqualToWithSortedQueryParameters(AssertionInfo, URL, URL)} (org.assertj.core.api.AssertionInfo, java.net.URL, java.net.URL)}  </code>
 * .
 *
 * @author SUN Ting
 */
@DisplayName("assertIsEqualToWithSortedQueryParameters")
public class Urls_assertIsEqualToWithSortedQueryParameters_Test extends UrlsBaseTest {

  // @format:off
  @ParameterizedTest
  @CsvSource({
      "https://example.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?name=ferret&color=purple#hello",

      "https://example.com/path/to/page#hello, " +
      "https://example.com/path/to/page#hello",

      "https://example.com/path/to/page, " +
      "https://example.com/path/to/page"
  })
  // @format:on
  public void should_pass_if_urls_equivalent_regardless_of_parameters_order(String actual, String expected) throws Exception {
    // GIVEN
    URL actualUrl = new URL(actual);
    URL expectedUrl = new URL(expected);
    // WHEN / THEN
    urls.assertIsEqualToWithSortedQueryParameters(info, actualUrl, expectedUrl);
  }

  // @format:off
  @ParameterizedTest
  @CsvSource({
      "http://example.com/path/to/page?color=purple&name=ferret#hello, " +
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example2.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/sunt/to/ting?color=purple&name=ferret#hello, " +
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?color=purple&name=ferret, "+
      "https://example.com/path/to/page?color=red&name=ferret",

      "https://example.com/path/to/page?color=purple&name=ferret#world, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?color=purple&name=ferret, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?color=red&name=jackson#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?"
  })
  // @format:on
  public void should_fail_if_urls_different_regardless_of_parameters_order(String actual, String expected) throws Exception {
    // GIVEN
    URL actualUrl = new URL(actual);
    URL expectedUrl = new URL(expected);
    // WHEN
    expectAssertionError(() -> urls.assertIsEqualToWithSortedQueryParameters(info, actualUrl, expectedUrl));
    // THEN
    verify(failures).failure(info, shouldBeEqualToWithSortedQueryParameters(actualUrl, expectedUrl));
  }

}
