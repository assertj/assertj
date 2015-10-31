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
package org.assertj.core.api.map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link MapAssert#isEqualTo(Object)}</code>.
 *
 * @author Vojislav Marinkovic
 */
public class MapAssert_isEqualTo_Test extends MapAssertBaseTest {

    private Map<String, Integer> expected = new HashMap<String, Integer>();
    @Override
    protected MapAssert<Object, Object> invoke_api_method() {
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        return assertions.isEqualTo(expected);
    }

    @Override
    protected void verify_internal_effects() {
        verify(maps).assertEqual(getInfo(assertions), getActual(assertions), expected, getObjects(assertions).getComparisonStrategy());
    }
}
