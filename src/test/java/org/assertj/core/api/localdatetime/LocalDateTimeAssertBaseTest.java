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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.localdatetime;

import java.time.LocalDateTime;

import org.assertj.core.api.AbstractLocalDateTimeAssert;
import org.assertj.core.api.BaseTest;


/**
 * 
 * Base test class for {@link AbstractLocalDateTimeAssert} tests.
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalDateTimeAssertBaseTest extends BaseTest {

  public static final LocalDateTime REFERENCE = LocalDateTime.of(2000, 12, 14, 0, 0);
  public static final LocalDateTime BEFORE = LocalDateTime.of(2000, 12, 13, 23, 59, 59, 999);
  public static final LocalDateTime AFTER = LocalDateTime.of(2000, 12, 14, 0, 0, 0, 1);

}