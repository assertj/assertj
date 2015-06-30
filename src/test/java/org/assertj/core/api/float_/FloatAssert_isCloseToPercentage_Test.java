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
package org.assertj.core.api.float_;

import org.assertj.core.api.FloatAssert;
import org.assertj.core.api.FloatAssertBaseTest;
import org.assertj.core.data.Percentage;

import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.verify;

public class FloatAssert_isCloseToPercentage_Test extends FloatAssertBaseTest {

    private final Percentage percentage = withPercentage(5.0f);
    private final Float value = 10.0f;

    @Override
    protected FloatAssert invoke_api_method() {
        return assertions.isCloseTo(value, percentage);
    }

    @Override
    protected void verify_internal_effects() {
        verify(floats).assertIsCloseToPercentage(getInfo(assertions), getActual(assertions), value, percentage);
    }
}
