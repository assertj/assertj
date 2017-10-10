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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldNotHaveAnyElementsOfTypes.shouldNotHaveAnyElementsOfTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.internal.TestDescription;
import org.junit.Test;

public class ShouldNotHaveAnyElementsOfTypes_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    List<Number> actual = new ArrayList<>();
    actual.add(1);
    actual.add(2);
    actual.add(3.0);
    actual.add(4.1);
    actual.add(BigDecimal.ONE);

    Class<?>[] unexpectedTypes = { Long.class, Double.class, BigDecimal.class };

    Map<Class<?>, List<Object>> nonMatchingElementsByType = new LinkedHashMap<>();
    nonMatchingElementsByType.put(BigDecimal.class, asObjectList(BigDecimal.ONE));
    nonMatchingElementsByType.put(Double.class, asObjectList(3.0, 4.1));

    ShouldNotHaveAnyElementsOfTypes shouldNotHaveAnyElementsOfTypes = shouldNotHaveAnyElementsOfTypes(actual,
                                                                                                      unexpectedTypes,
                                                                                                      nonMatchingElementsByType);
    // WHEN
    String message = shouldNotHaveAnyElementsOfTypes.create(new TestDescription("Test"), CONFIGURATION_PROVIDER.representation());

    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <[1, 2, 3.0, 4.1, 1]>%n" +
                                         "to not have any elements of the following types:%n" +
                                         "  <[java.lang.Long, java.lang.Double, java.math.BigDecimal]>%n" +
                                         "but found:%n" +
                                         "  <{java.math.BigDecimal=[1], java.lang.Double=[3.0, 4.1]}>"));
  }

  private static List<Object> asObjectList(Object... objects) {
    return Arrays.asList(objects);
  }
}
