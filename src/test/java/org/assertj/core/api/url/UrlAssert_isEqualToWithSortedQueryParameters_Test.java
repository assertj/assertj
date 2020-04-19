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

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for <code>{@link org.assertj.core.api.UrlAssert#isEqualToWithSortedQueryParameters(URL)} </code>.
 */
public class UrlAssert_isEqualToWithSortedQueryParameters_Test {
  @Test
  public void should_be_equal_to_if_only_query_parameters_are_different() throws MalformedURLException {
    assertThat(new URL("https://example.com/path/to/page?color=purple&name=ferret"))
      .isEqualToWithSortedQueryParameters(new URL("https://example.com/path/to/page?name=ferret&color=purple"));
  }
}
