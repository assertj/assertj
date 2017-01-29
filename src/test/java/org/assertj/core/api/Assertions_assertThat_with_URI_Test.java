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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.Assertions#assertThat(java.net.URI)}</code>.
 *
 * @author Alexander Bischof
 */
public class Assertions_assertThat_with_URI_Test {

  private static URI uri;

  @BeforeClass
  public static void beforeClass() throws URISyntaxException {
    uri = new URI("http://www.helloworld.org:8080/pages");
  }

  @Test
  public void should_create_Assert() {
    AbstractUriAssert<?> assertions = Assertions.assertThat(uri);
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() {
    AbstractUriAssert<?> assertions = Assertions.assertThat(uri);
    assertThat(assertions.actual).isSameAs(uri);
  }
}
