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
package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldBeEqualToWithSortedQueryParameters.shouldBeEqualToWithSortedQueryParameters;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("shouldBeEqualToWithSortedQueryParameters")
public class ShouldBeEqualToWithSortedQueryParameters_create_Test {

  @Test
  public void should_create_error_message() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("https://example2.com/path/to/page?name=ferret&color=purple");
    URL expected = new URL("https://example.com/path/to/page?color=purple&name=ferret");
    // WHEN
    String error = shouldBeEqualToWithSortedQueryParameters(actual, expected).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting URL to be:%n" +
                                 "  <\"https://example.com/path/to/page?color=purple&name=ferret\">%n" +
                                 "but was:%n" +
                                 "  <\"https://example2.com/path/to/page?name=ferret&color=purple\">%n" +
                                 "after sorting parameters"));
  }
}
