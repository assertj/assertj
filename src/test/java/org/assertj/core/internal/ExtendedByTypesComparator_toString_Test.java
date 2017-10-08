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
package org.assertj.core.internal;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtendedByTypesComparator_toString_Test {

  @Test
  public void should_return_description_of_FieldByFieldComparator() {
    assertThat(new ExtendedByTypesComparator(new FieldByFieldComparator(), new TypeComparators()).toString())
      .matches("field/property by field/property comparator on all fields/properties");
  }

  @Test
  public void should_return_description_of_FieldByFieldComparator_and_extended_types() {
    TypeComparators comparatorsByType = new TypeComparators();
    comparatorsByType.put(BigDecimal.class, new BigDecimalComparator());
    assertThat(new ExtendedByTypesComparator(new FieldByFieldComparator(), comparatorsByType).toString())
      .matches("extended field/property by field/property comparator on all fields/properties for types \\{class java.math.BigDecimal=.*}");
  }
}
