/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.kotlin.testkit.junit.jupiter

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DefaultDisplayNameGeneratorTest {

    private val underTest = DefaultDisplayNameGenerator()

    @ParameterizedTest
    @CsvSource(
        "org.assertj.tests.core.kotlin.testkit.junit.jupiter.SomeAssert_someMethod_Test, SomeAssert someMethod",
        "org.assertj.tests.core.kotlin.testkit.junit.jupiter.SomeAssert_someMethod_with_SomeType_Test, SomeAssert someMethod with SomeType"
    )
    fun generateDisplayNameForClass_should_remove_test_suffix(testClass: Class<*>, expected: String?) {
        // WHEN
        val displayName = underTest.generateDisplayNameForClass(testClass)
        // THEN
        then(displayName).isEqualTo(expected)
    }
}

@Suppress("unused")
internal class SomeAssert_someMethod_Test

@Suppress("unused")
internal class SomeAssert_someMethod_with_SomeType_Test 
