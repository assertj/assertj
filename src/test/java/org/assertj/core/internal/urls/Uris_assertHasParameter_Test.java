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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;

import java.net.URI;
import java.util.List;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

public class Uris_assertHasParameter_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_parameter_is_missing() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news");
    String name = "article";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name).create());
  }

  @Test
  public void should_pass_if_parameter_has_no_value() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article");
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article");
  }

  @Test
  public void should_pass_if_parameter_has_value() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=10");
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article");
  }

  @Test
  public void should_fail_if_parameter_without_value_is_missing() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news");
    String name = "article";
    String expectedValue = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue).create());
  }

  @Test
  public void should_pass_if_parameter_without_value_is_found() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article");
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article", null);
  }

  @Test
  public void should_fail_if_parameter_without_value_has_value() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=11");
    String name = "article";
    String expectedValue = null;
    List<String> actualValue = list("11");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue, actualValue).create());
  }

  @Test
  public void should_fail_if_parameter_without_value_has_multiple_values() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=11&article=12");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList("11", "12");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue, actualValues).create());
  }

  @Test
  public void should_fail_if_parameter_with_value_is_missing() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news");
    String name = "article";
    String expectedValue = "10";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue).create());
  }

  @Test
  public void should_fail_if_parameter_with_value_has_no_value() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = "10";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue, list((String) null)).create());

  }

  @Test
  public void should_fail_if_parameter_with_value_has_multiple_no_values() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article&article");
    String name = "article";
    String expectedValue = "10";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue, list(null, null)).create());
  }

  @Test
  public void should_fail_if_parameter_with_value_is_wrong() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=11");
    String name = "article";
    String expectedValue = "10";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expectedValue, list("11")).create());
  }

  @Test
  public void should_pass_if_parameter_with_value_is_found() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=10");
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article", "10");
  }

  @Test
  public void should_pass_if_parameter_with_escaped_value_is_found() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=abc%26page%3D5");
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article", "abc&page=5");
  }

}
