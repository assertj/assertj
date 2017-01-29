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
import static org.assertj.core.api.Assertions.withinPercentage;

import org.junit.Test;

public class Assertions_withinPercentage_Test {

    @Test
    public void should_create_double() {
        assertThat(withinPercentage(1d)).isNotNull();
    }

    @Test
    public void should_create_integer() {
        assertThat(withinPercentage(1)).isNotNull();
    }

    @Test
    public void should_create_long() {
        assertThat(withinPercentage(1L)).isNotNull();
    }

}
