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
package org.assertj.core.api.offsetdatetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

import org.assertj.core.api.AbstractOffsetDateTimeAssertBaseTest;
import org.assertj.core.api.OffsetDateTimeAssert;
import org.assertj.core.internal.OffsetDateTimeByInstantComparator;

public class OffsetDateTimeAssert_usingDefaultComparator_Test extends AbstractOffsetDateTimeAssertBaseTest {

  @Override
  protected OffsetDateTimeAssert invoke_api_method() {
    return assertions.usingComparator(OffsetDateTime::compareTo).usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getComparables(assertions).getComparator()).isSameAs(OffsetDateTimeByInstantComparator.getInstance());
    assertThat(getObjects(assertions).getComparator()).isNull();
  }
}
