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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameters;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;

public class Urls_assertHasNoParameter_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_parameter_is_missing() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news");
    String name = "article";
    // WHEN/THEN
    urls.assertHasNoParameter(info, url, name);
  }

  @Test
  public void should_fail_if_parameter_is_present_without_value() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article");
    String name = "article";
    List<String> actualValues = newArrayList((String) null);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameter(info, url, name));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(url, name, actualValues).create());
  }

  @Test
  public void should_fail_if_parameter_is_present_with_value() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article=10");
    String name = "article";
    List<String> actualValues = newArrayList("10");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameter(info, url, name));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(url, name, actualValues).create());
  }

  @Test
  public void should_fail_if_parameter_is_present_multiple_times() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article&article=10");
    String name = "article";
    List<String> actualValues = newArrayList(null, "10");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameter(info, url, name));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(url, name, actualValues).create());
  }

  @Test
  public void should_pass_if_parameter_without_value_is_missing() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news");
    String name = "article";
    String unwantedValue = null;
    // WHEN/THEN
    urls.assertHasNoParameter(info, url, name, unwantedValue);
  }

  @Test
  public void should_fail_if_parameter_without_value_is_present() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList((String) null);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameter(info, url, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(url, name, expectedValue, actualValues).create());
  }

  @Test
  public void should_pass_if_parameter_without_value_is_present_with_value() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news=10");
    String name = "article";
    String unwantedValue = null;
    // WHEN/THEN
    urls.assertHasNoParameter(info, url, name, unwantedValue);
  }

  @Test
  public void should_pass_if_parameter_with_value_is_missing() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news");
    String name = "article";
    String unwantedValue = "10";
    // WHEN/THEN
    urls.assertHasNoParameter(info, url, name, unwantedValue);
  }

  @Test
  public void should_pass_if_parameter_with_value_is_present_without_value() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article");
    String name = "article";
    String unwantedValue = "10";
    // WHEN/THEN
    urls.assertHasNoParameter(info, url, name, unwantedValue);
  }

  @Test
  public void should_pass_if_parameter_with_value_is_present_with_wrong_value() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article=11");
    String name = "article";
    String unwantedValue = "10";
    // WHEN/THEN
    urls.assertHasNoParameter(info, url, name, unwantedValue);
  }

  @Test
  public void should_fail_if_parameter_with_value_is_present() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article=10");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList("10");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameter(info, url, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(url, name, expectedValue, actualValues).create());
  }

  @Test
  public void should_pass_if_url_has_no_parameters() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news");
    // WHEN/THEN
    urls.assertHasNoParameters(info, url);
  }

  @Test
  public void should_fail_if_url_has_some_parameters() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article=10&locked=false");
    Set<String> actualValues = newLinkedHashSet("article", "locked");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameters(info, url));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameters(url, actualValues).create());
  }

  @Test
  public void should_fail_if_url_has_one_parameter() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?article=10");
    Set<String> actualValues = newLinkedHashSet("article");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoParameters(info, url));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameters(url, actualValues).create());
  }
}
