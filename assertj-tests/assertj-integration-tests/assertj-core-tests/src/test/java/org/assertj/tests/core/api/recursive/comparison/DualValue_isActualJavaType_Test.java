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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_isActualJavaType_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest(name = "type: {1}")
  @MethodSource("java_types")
  void isActualJavaType_should_return_true_when_actual_is_a_java_type(Object actual, @SuppressWarnings("unused") String type) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, Pair.of(1, "a"));
    // WHEN
    boolean isActualJavaType = dualValue.isActualJavaType();
    // THEN
    then(isActualJavaType).isTrue();
  }

  static Stream<Arguments> java_types() throws Exception {
    return Stream.of(arguments(DatatypeFactory.newInstance().newXMLGregorianCalendar(),
                               "com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl"),
                     arguments(InetAddress.getByName("127.0.0.1"), InetAddress.class.getName()),
                     arguments("", String.class.getName()),
                     arguments(new ServletException(), ServletException.class.getName()));
  }

  @Test
  void isActualJavaType_should_return_false_when_actual_is_not_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), "");
    // WHEN
    boolean isActualJavaType = dualValue.isActualJavaType();
    // THEN
    then(isActualJavaType).isFalse();
  }

  @Test
  void isActualJavaType_should_return_false_when_actual_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, "");
    // WHEN
    boolean isActualJavaType = dualValue.isActualJavaType();
    // THEN
    then(isActualJavaType).isFalse();
  }

}
