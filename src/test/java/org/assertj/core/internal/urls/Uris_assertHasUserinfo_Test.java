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
 * Tests for <code>{@link org.assertj.core.internal.Uris#assertHasUserInfo(org.assertj.core.api.AssertionInfo, java.net.URI, String)}  </code>.
 *
 * @author Alexander Bischof
 */
public class Uris_assertHasUserInfo_Test extends UrisBaseTest {

    @Test
    public void should_fail_if_actual_is_null() {
        thrown.expectAssertionError(actualIsNull());
        uris.assertHasUserInfo(info, null, "http://test:pass@www.helloworld.org/index.html");
    }

    @Test
    public void should_fail_if_actual_is_not_null_but_expected_is_null() throws URISyntaxException {
        thrown.expectAssertionError("expected:<null> but was:<\"test:pass\">");
        uris.assertHasUserInfo(info, new URI("http://test:pass@www.helloworld.org/index.html"), null);
    }

    @Test
    public void should_throw_error_if_actual_uri_has_no_userinfo() throws URISyntaxException {
        thrown.expectNullPointerException(null);
        uris.assertHasUserInfo(info, new URI("http://www.helloworld.org/index.html"), "test:pass");
    }

    @Test
    public void should_pass_if_actual_uri_has_no_userinfo_and_given_is_null() throws URISyntaxException {
        uris.assertHasUserInfo(info, new URI("http://www.helloworld.org/index.html"), null);
    }

    @Test
    public void should_pass_if_actual_uri_has_the_given_userinfo_with_pages() throws URISyntaxException {
        uris.assertHasUserInfo(info, new URI("http://test:pass@www.helloworld.org/index.html"), "test:pass");
    }

    @Test
    public void should_throw_error_if_actual_uri_has_no_scheme() throws URISyntaxException {
        thrown.expectNullPointerException(null);
        uris.assertHasUserInfo(info, new URI("http://test:pass@www.helloworld.org/index.html"), "test:pass");
    }

    @Test
    public void should_throw_error_if_urisyntax_is_not_valid() throws URISyntaxException {
        thrown.expect(URISyntaxException.class);
        uris.assertHasUserInfo(info, new URI("http://finance.yahoo.com/q/h?s=^IXIC"), "http");
    }
}
