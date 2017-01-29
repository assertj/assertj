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
package org.assertj.core.api.double_;

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.assertj.core.data.Percentage;

import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link DoubleAssert#isNotCloseTo(Double, Percentage)}</code>.
 *
 * @author Chris Arnott
 */
public class DoubleAssert_isNotCloseToPercentage_Test extends DoubleAssertBaseTest {

    private final Percentage percentage = withPercentage(5.0);
    private final Double value = 10.0;

    @Override
    protected DoubleAssert invoke_api_method() {
        return assertions.isNotCloseTo(value, percentage);
    }

    @Override
    protected void verify_internal_effects() {
        verify(doubles).assertIsNotCloseToPercentage(getInfo(assertions), getActual(assertions), value, percentage);
    }
}
