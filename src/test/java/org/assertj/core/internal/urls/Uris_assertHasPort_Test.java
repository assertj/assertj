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
 * Tests for <code>{@link org.assertj.core.internal.Uris#assertHasPort(org.assertj.core.api.AssertionInfo, java.net.URI, int)}</code>.
 *
 * @author Alexander Bischof
 */
public class Uris_assertHasPort_Test extends UrisBaseTest {

    @Test
    public void should_fail_if_actual_is_null() {
        thrown.expectAssertionError(actualIsNull());
        uris.assertHasPort(info, null, 8080);
    }

    @Test
    public void should_pass_if_actual_uri_has_the_given_port() throws URISyntaxException {
        uris.assertHasPort(info, new URI("http://example.com:8080/pages/"), 8080);
    }

    @Test
    public void should_throw_error_if_port_is_not_equal() throws URISyntaxException {
        thrown.expect(AssertionError.class);
        uris.assertHasPort(info, new URI("http://example.com:8080"), 0);
    }

    @Test
    public void should_throw_error_if_port_is_not_present() throws URISyntaxException {
        thrown.expect(AssertionError.class);
        uris.assertHasPort(info, new URI("http://example.com"), 0);
    }

    @Test
    public void should_throw_error_if_urisyntax_is_not_valid() throws URISyntaxException {
        thrown.expect(URISyntaxException.class);
        uris.assertHasPort(info, new URI("http://finance.yahoo.com:8080/q/h?s=^IXIC"), 8080);
    }

    @Test
    public void should_throw_error_if_actual_uri_has_no_scheme() throws URISyntaxException {
        thrown.expectNullPointerException(null);
        uris.assertHasPort(info, new URI("http://example.com:8080/pages/"), 8080);
    }
}
