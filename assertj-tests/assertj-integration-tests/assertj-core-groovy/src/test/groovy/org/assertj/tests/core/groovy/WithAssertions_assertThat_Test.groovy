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
package org.assertj.tests.core.groovy

import groovy.transform.CompileStatic
import org.assertj.core.api.AbstractStringAssert
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

import static org.mockito.Answers.CALLS_REAL_METHODS
import static org.mockito.Mockito.mock

@CompileStatic
class WithAssertions_assertThat_Test {

  private final WithAssertions underTest = mock(WithAssertions, CALLS_REAL_METHODS)

  @Test
  void should_accept_dollar_slashy_strings() {
    // GIVEN
    def actual = $/.*foo.*/$
    // WHEN
    AbstractStringAssert<?> result = underTest.assertThat(actual)
    // THEN
    result.isEqualTo(".*foo.*")
  }

  @Test
  void should_accept_slashy_strings() {
    // GIVEN
    def actual = /.*foo.*/
    // WHEN
    AbstractStringAssert<?> result = underTest.assertThat(actual)
    // THEN
    result.isEqualTo(".*foo.*")
  }

  @Test
  void should_accept_triple_single_quoted_strings() {
    // GIVEN
    def actual = '''
        .*foo.*
        '''.stripIndent()
    // WHEN
    AbstractStringAssert<?> result = underTest.assertThat(actual)
    // THEN
    result.isEqualTo("\n.*foo.*\n")
  }

}
