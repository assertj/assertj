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

import static org.assertj.core.util.Lists.newArrayList;

import org.junit.Rule;
import org.junit.Test;

public class JUnitSoftAssertionsSuccessTest {

  @Rule
  public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

  @Test
  public void all_assertions_should_pass() throws Throwable {
	softly.assertThat(1).isEqualTo(1);
	softly.assertThat(newArrayList(1, 2)).containsOnly(1, 2);
  }

}
