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
import org.assertj.core.util.VisibleForTesting;

import java.net.URI;

/**
 * Base class for all implementations of assertions for {@link URI}s.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *            for more details.
 * @author Alexander Bischof
 * @see java.net.URI
 */
public abstract class AbstractUriAssert<S extends AbstractUriAssert<S>> extends AbstractAssert<S, URI> {

    @VisibleForTesting
    protected Uris uris = Uris.instance();

    protected AbstractUriAssert(final URI actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@code URI} has the expected scheme.
     * <p/>
     * This assertion will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org")).hasScheme("http");
     * assertThat(new URI("ftp://helloworld.org")).hasScheme("ftp");
     * assertThat(new URI("helloworld.org")).hasScheme(null);
     * </code></pre>
     * <p/>
     * Whereas this assertion will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org")).hasScheme("ftp");
     * assertThat(new URI("http://helloworld.org")).hasScheme(null);
     * assertThat((URI)null).hasScheme(null);
     * </code></pre>
     *
     * @param expected the expected scheme of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual scheme is not equal to the expected scheme.
     * @throws java.net.URISyntaxException    if actual can not be parsed as a URI reference.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasScheme(String expected) {
        uris.assertHasScheme(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected path.
     * <p/>
     * This assertion will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org/pages")).hasPath("/pages/")
     * </code></pre>
     * <p/>
     * Whereas this assertion will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org/pickme")).hasPath("/pages/")
     * </code></pre>
     *
     * @param expected the expected path of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual path is not equal to the expected path.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasPath(String expected) {
        uris.assertHasPath(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has no path. This method is a convinience method for
     *
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org")).hasPath("");
     * </code></pre>
     *
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual has no path.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasNoPath() {
        uris.assertHasPath(info, actual, "");
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected path.
     * <p/>
     * This assertion will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org:8080")).hasPort(8080);
     * assertThat(new URI("http://helloworld.org")).hasPort(-1);
     * </code></pre>
     * <p/>
     * Whereas this assertion will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org")).hasPort(0);
     * assertThat(new URI("helloworld.org")).hasPort(0);
     * assertThat(new URI("helloworld.org:8080")).hasPort(0);
     * assertThat(new URI("helloworld.org:8080000")).hasPort(0);
     * assertThat(new URI("helloworld.org/pages:8080000")).hasPort(0);
     * </code></pre>
     *
     * @param expected the expected port of the actual {@code URI}. Use -1 for no port.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual port is not equal to the expected path.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasPort(int expected) {
        uris.assertHasPort(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has no port.
     *
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasNoPort();
     * </code></pre>
     *
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual has no fragment.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasNoPort() {
        uris.assertHasPort(info, actual, -1);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected host.
     * <p/>
     * These assertions will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org:8080")).hasHost("helloworld.org");
     * assertThat(new URI("http://helloworld.org")).hasHost("helloworld.org");
     * assertThat(new URI("http://www.helloworld.org:8080")).hasHost("www.helloworld.org");
     * </code></pre>
     * <p/>
     * Whereas these assertions will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org:8080")).hasHost("helloworld.org");
     * assertThat(new URI("http://helloworld.org:8080")).hasHost("www.helloworld.org");
     * assertThat(new URI("http://www.helloworld.org/pages")).hasHost("helloworld.org");
     * </code></pre>
     *
     * @param expected the expected host of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual host is not equal to the expected host.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasHost(String expected) {
        uris.assertHasHost(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected authority.
     * <p/>
     * These assertions will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org:8080")).hasAuthority("helloworld.org:8080");
     * assertThat(new URI("http://www.helloworld.org:8080")).hasAuthority("www.helloworld.org:8080");
     * </code></pre>
     * <p/>
     * Whereas these assertions will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org:8080")).hasAuthority("www.helloworld.org");
     * assertThat(new URI("http://www.helloworld.org")).hasAuthority("www.helloworld.org:8080");
     * </code></pre>
     *
     * @param expected the expected authority of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual authority is not equal to the expected authority.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasAuthority(String expected) {
        uris.assertHasAuthority(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected fragment.
     * <p/>
     * This assertion will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org:8080/index.html#print")).hasFragment("print");
     * </code></pre>
     * <p/>
     * Whereas this assertion will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://helloworld.org:8080/index.html#print")).hasFragment("hello");
     * </code></pre>
     *
     * @param expected the expected fragment of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual fragment is not equal to the expected fragment.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasFragment(String expected) {
        uris.assertHasFragment(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has no fragment. This method is a convinience method for
     *
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasFragment(null);
     * </code></pre>
     *
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual has no fragment.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasNoFragment() {
        uris.assertHasFragment(info, actual, null);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected query.
     * <p/>
     * This assertion will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org/index.html?type=test")).hasQuery("type=test");
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasQuery(null);
     * </code></pre>
     * <p/>
     * Whereas this assertion will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org/index.html?type=test")).hasQuery("type=hello");
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasQuery("type=hello");
     * assertThat(new URI("http://www.helloworld.org/index.html?type=test")).hasQuery(null);
     * </code></pre>
     *
     * @param expected the expected query of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual query is not equal to the expected query.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasQuery(String expected) {
        uris.assertHasQuery(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has no query. This method is a convinience method for
     *
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasQuery(null);
     * </code></pre>
     *
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual has no query.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasNoQuery() {
        uris.assertHasQuery(info, actual, null);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has the expected userinfo.
     * <p/>
     * These assertions will succeed:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://test:pass@www.helloworld.org/index.html")).hasUserInfo("test:pass");
     * assertThat(new URI("http://test@www.helloworld.org/index.html")).hasUserInfo("test");
     * assertThat(new URI("http://:pass@www.helloworld.org/index.html")).hasUserInfo(":pass");
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasUserInfo(null);
     * </code></pre>
     * <p/>
     * Whereas this assertion will fail:
     * <p/>
     * <pre><code class='java'>
     * assertThat(new URI("http://test:pass@www.helloworld.org/index.html")).hasUserInfo(null);
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasUserInfo("test:pass");
     * assertThat(new URI("http://test:pass@www.helloworld.org/index.html")).hasUserInfo("test2:pass2");
     * </code></pre>
     *
     * @param expected the expected userinfo of the actual {@code URI}.
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual userinfo is not equal to the expected userinfo.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasUserInfo(String expected) {
        uris.assertHasUserInfo(info, actual, expected);
        return myself;
    }

    /**
     * Verifies that the actual {@code URI} has no userinfo. This method is a convinience method for
     *
     * <pre><code class='java'>
     * assertThat(new URI("http://www.helloworld.org/index.html")).hasUserInfo(null);
     * </code></pre>
     *
     * @return {@code this} assertion object.
     * @throws AssertionError                 if the actual has no userinfo.
     * @throws java.lang.NullPointerException if the actual {@code URI} has no scheme.
     */
    public S hasNoUserInfo() {
        uris.assertHasUserInfo(info, actual, null);
        return myself;
    }
}
