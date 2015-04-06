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
package org.assertj.core.api;

import org.assertj.core.internal.Uris;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mock;

/**
 * Base class for {@link UriAssert} tests.
 */
public abstract class UriAssertBaseTest extends BaseTestTemplate<UriAssert, URI> {

  protected Uris uris;

  @Override
  protected UriAssert create_assertions() {
      try {
          return new UriAssert(new URI("example.com/pages/"));
      } catch (URISyntaxException e) {
          e.printStackTrace();
      }
      return null;
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    uris = mock(Uris.class);
    assertions.uris = uris;
  }

  protected Uris getUris(UriAssert someAssertions) {
    return someAssertions.uris;
  }
}
