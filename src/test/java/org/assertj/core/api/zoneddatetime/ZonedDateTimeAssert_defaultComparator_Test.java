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
package org.assertj.core.api.zoneddatetime;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractZonedDateTimeAssertBaseTest;
import org.assertj.core.api.ZonedDateTimeAssert;
import org.assertj.core.util.temporal.DefaultZonedDateTimeComparator;

import java.util.Comparator;

public class ZonedDateTimeAssert_defaultComparator_Test extends AbstractZonedDateTimeAssertBaseTest {

  @Override
  protected void inject_internal_objects() {
  }

  @Override
  protected ZonedDateTimeAssert invoke_api_method() {
    return assertions;
  }

  @Override
  protected void verify_internal_effects() {
    Comparator<?> defaultComparator = getComparables(assertions).getComparator();
    assertThat(defaultComparator).isEqualTo(DefaultZonedDateTimeComparator.getInstance());
    assertThat(defaultComparator).hasToString("default ZonedDateTime comparison by instant");
    assertThat(getObjects(assertions).getComparator()).isNull();
  }
}
