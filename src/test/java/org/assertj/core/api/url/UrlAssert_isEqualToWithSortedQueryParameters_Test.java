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
package org.assertj.core.api.url;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Test for <code>{@link org.assertj.core.api.UrlAssert#isEqualToWithSortedQueryParameters(URL)} </code>.
 */

@DisplayName("UrlAssert isEqualToWithSortedQueryParameters")
public class UrlAssert_isEqualToWithSortedQueryParameters_Test {

  @Test
  public void should_pass_if_only_query_parameters_orders_are_different() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("https://example.com/path/to/page?color=purple&name=ferret");
    URL expected = new URL("https://example.com/path/to/page?name=ferret&color=purple");
    // WHEN/THEN
    assertThat(actual).isEqualToWithSortedQueryParameters(expected);
  }

  @Test
  public void should_fail_if_domains_are_different() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("https://example.com/path/to/page?color=purple&name=ferret");
    URL expected = new URL("https://example2.com/path/to/page?color=purple&name=ferret");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualToWithSortedQueryParameters(expected));
    // THEN
    then(assertionError).hasMessage("%nExpecting URL to be:%n  <\"%s\">%nbut actually is:%n  <\"%s\">", expected.toString(),
                                    actual.toString());
  }

  @Test
  public void should_fail_if_sorted_query_parameters_are_different() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("https://example.com/path/to/page?color=purple&name=ferret");
    URL expected = new URL("https://example.com/path/to/page?color=purple");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualToWithSortedQueryParameters(expected));
    // THEN
    then(assertionError).hasMessage("%nExpecting URL to be:%n  <\"%s\">%nbut actually is:%n  <\"%s\">", expected.toString(),
                                    actual.toString());
  }

}
