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

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for <code>{@link org.assertj.core.api.Assertions#assertThat(java.util.Optional)}</code>.
 *
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Alexander Bischof
 */
public class Assertions_assertThat_with_Optional_Test {

    private Optional<String> actual;

    @Before
    public void before(){
        actual = Optional.of("String");
    }

    @Test
    public void should_create_Assert() {
        OptionalAssert<String> assertions = Assertions.assertThat(actual);
        assertNotNull(assertions);
    }

    @Test
    public void should_pass_actual() {
        OptionalAssert<String> assertions = Assertions.assertThat(actual);
        assertSame(actual, assertions.actual);
    }
}
