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
package org.assertj.core.internal.urls;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * Tests for <code>{@link org.assertj.core.internal.Uris#assertHasAuthority(org.assertj.core.api.AssertionInfo, java.net.URI, String)} </code>.
 *
 * @author Alexander Bischof
 */
public class Uris_assertHasAuthority_Test extends UrisBaseTest {

    @Test
    public void should_fail_if_actual_is_null() {
        thrown.expectAssertionError(actualIsNull());
        uris.assertHasAuthority(info, null, "http://www.helloworld.org");
    }

    @Test
    public void should_fail_if_actual_uri_has_port_but_given_has_not() throws URISyntaxException {
        thrown.expect(AssertionError.class);
        uris.assertHasAuthority(info, new URI("http://www.helloworld.org:8080"), "www.helloworld.org");
    }

    @Test
    public void should_fail_if_actual_uri_has_not_port_but_given_has() throws URISyntaxException {
        thrown.expect(AssertionError.class);
        uris.assertHasAuthority(info, new URI("http://www.helloworld.org"), "www.helloworld.org:8080");
    }

    @Test
    public void should_pass_if_actual_uri_has_the_given_authority() throws URISyntaxException {
        uris.assertHasAuthority(info, new URI("http://www.helloworld.org:8080"), "www.helloworld.org:8080");
    }

    @Test
    public void should_pass_if_actual_uri_has_the_given_authority_with_pages() throws URISyntaxException {
        uris.assertHasAuthority(info, new URI("http://www.helloworld.org:8080/pages"), "www.helloworld.org:8080");
    }

    @Test
    public void should_throw_error_if_actual_uri_has_no_scheme() throws URISyntaxException {
        thrown.expectNullPointerException(null);
        uris.assertHasAuthority(info, new URI("helloworld.org/pages"), "helloworld.org");
    }

    @Test
    public void should_throw_error_if_urisyntax_is_not_valid() throws URISyntaxException {
        thrown.expect(URISyntaxException.class);
        uris.assertHasAuthority(info, new URI("http://finance.yahoo.com/q/h?s=^IXIC"), "http");
    }
}
