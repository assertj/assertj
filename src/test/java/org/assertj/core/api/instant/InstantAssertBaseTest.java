/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.instant;

import java.time.Instant;

import org.assertj.core.api.BaseTest;


public class InstantAssertBaseTest extends BaseTest {
  public static final Instant BEFORE = Instant.now().minusSeconds(1);
  public static final Instant REFERENCE = Instant.now();
  public static final Instant AFTER = Instant.now().plusSeconds(1);
}
