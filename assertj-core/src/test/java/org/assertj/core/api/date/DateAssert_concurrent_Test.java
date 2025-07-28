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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.IntStream;

import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Tests the default date format used when using date assertions happen concurrently.
 *
 * @author Niklas Keller
 */
class DateAssert_concurrent_Test extends DateAssertBaseTest {

  @Test
  void date_assertion_concurrently() {
    Date date = DateUtil.parse("2003-04-26");

    IntStream.range(0, 1000).parallel().forEach((int i) -> {
      assertThat(date).isEqualTo("2003-04-26");
    });
  }
}
