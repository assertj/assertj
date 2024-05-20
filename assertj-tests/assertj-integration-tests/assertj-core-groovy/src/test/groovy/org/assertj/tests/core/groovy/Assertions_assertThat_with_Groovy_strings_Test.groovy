/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.groovy

import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

class Assertions_assertThat_with_Groovy_strings_Test {

  @Test
  void should_accept_triple_single_quoted_strings() {
    // GIVEN
    def actual = '''
.*foo.*
'''
    // WHEN/THEN
    assertThat(actual).isEqualTo("\n.*foo.*\n")
  }

  @Test
  void should_accept_slashy_strings() {
    // GIVEN
    def actual = /.*foo.*/
    // WHEN/THEN
    assertThat(actual).isEqualTo(".*foo.*")
  }

  @Test
  void should_accept_dollar_slashy_strings() {
    // GIVEN
    def actual = $/.*foo.*/$
    // WHEN/THEN
    assertThat(actual).isEqualTo(".*foo.*")
  }

}
