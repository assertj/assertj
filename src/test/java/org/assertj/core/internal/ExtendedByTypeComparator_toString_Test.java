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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtendedByTypeComparator_toString_Test {

  @Test
  public void should_return_description_of_FieldByFieldComparator() {
    Map<Class<?>, Comparator<?>> comparatorsByType = new HashMap<>();
    comparatorsByType.put(BigDecimal.class, new BigDecimalComparator());
    assertThat(new ExtendedByTypeComparator(new FieldByFieldComparator(), comparatorsByType))
      .hasToString("extended field/property by field/property comparator on all fields/properties for types [class java.math.BigDecimal]");
  }
}
