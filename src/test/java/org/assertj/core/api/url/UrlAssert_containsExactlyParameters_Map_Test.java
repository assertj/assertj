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
package org.assertj.core.api.url;

import org.assertj.core.api.UrlAssert;
import org.assertj.core.api.UrlAssertBaseTest;
import org.assertj.core.data.MapEntry;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@DisplayName("UrlAssert containsExactlyParameters")
class UrlAssert_containsExactlyParameters_Map_Test extends UrlAssertBaseTest {

  private final Map<String, List<String>> expected = Maps.mapOf(
    MapEntry.entry("a", Lists.list("v1", "v2")),
    MapEntry.entry("b", Lists.list("v1"))
  );

  @Override
  protected UrlAssert invoke_api_method() {
    return assertions.containsExactlyParameters(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(urls).assertContainsExactlyParameters(getInfo(assertions), getActual(assertions), expected);
  }

}
