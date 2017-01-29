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

public class JUnitBDDSoftAssertionsSuccessTest {

  @Rule
  public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

  @Test
  public void all_assertions_should_pass() throws Throwable {
	softly.then(1).isEqualTo(1);
	softly.then(newArrayList(1, 2)).containsOnly(1, 2);
  }

}
