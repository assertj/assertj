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
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import java.util.Comparator;
import java.util.Date;
import org.assertj.core.api.DateAssert;
import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link DateAssert#usingComparator(java.util.Comparator)}</code> and
 * <code>{@link DateAssert#usingDefaultComparator()}</code>.
 *
 * @author Joel Costigliola
 */
class DateAssert_usingComparator_Test extends DateAssertBaseTest {

  private Comparator<Date> comparator = alwaysEqual();

  @Test
  void using_default_comparator_test() {
    assertions.usingDefaultComparator();
    assertThat(getObjects(assertions)).isSameAs(Objects.instance());
    assertThat(Dates.instance()).isSameAs(getDates(assertions));
  }

  @Test
  void using_custom_comparator_test() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    assertions.usingComparator(comparator);
    assertThat(getObjects(assertions).getComparator()).isSameAs(comparator);
    assertThat(getDates(assertions).getComparator()).isSameAs(comparator);
  }
}
