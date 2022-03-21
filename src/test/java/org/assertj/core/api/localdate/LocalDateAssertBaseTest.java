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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.localdate;

import java.time.LocalDate;

import org.assertj.core.api.AbstractLocalDateAssert;

/**
 * 
 * Base test class for {@link AbstractLocalDateAssert} tests.
 * 
 */
public class LocalDateAssertBaseTest {

  public static final LocalDate BEFORE = LocalDate.now().minusDays(1);
  public static final LocalDate REFERENCE = LocalDate.now();
  public static final LocalDate AFTER = LocalDate.now().plusDays(1);

}