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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.file.Path;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.Test;

class PathAssert_content_with_charset_Test extends PathAssertBaseTest implements NavigationMethodBaseTest<PathAssert> {

  @Override
  protected PathAssert invoke_api_method() {
    assertions.content(UTF_8);
    return assertions;
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsReadable(getInfo(assertions), getActual(assertions));
  }

  @Override
  protected PathAssert create_assertions() {
    return new PathAssert(new File("src/test/resources/utf8.txt").toPath());
  }

  @Test
  public void should_return_StringAssert_on_path_content_with_given_charset() {
    // GIVEN
    Path path = new File("src/test/resources/utf8.txt").toPath();
    // WHEN
    AbstractStringAssert<?> stringAssert = assertThat(path).content(UTF_8);
    // THEN
    stringAssert.contains("é à");
  }

  @Override
  public PathAssert getAssertion() {
    return assertions;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(PathAssert assertion) {
    return assertion.content(UTF_8);
  }

}
