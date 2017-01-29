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

import java.util.OptionalLong;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for <code>{@link Assertions#assertThat(java.util.OptionalLong)}</code>.
 *
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Alexander Bischof
 */
public class Assertions_assertThat_with_OptionalLong_Test {

    private OptionalLong actual;

    @Before
    public void before(){
        actual = OptionalLong.of(10L);
    }

    @Test
    public void should_create_Assert() {
        assertNotNull(Assertions.assertThat(actual));
    }

    @Test
    public void should_pass_actual() {
        assertSame(actual, Assertions.assertThat(actual).actual);
    }
}
